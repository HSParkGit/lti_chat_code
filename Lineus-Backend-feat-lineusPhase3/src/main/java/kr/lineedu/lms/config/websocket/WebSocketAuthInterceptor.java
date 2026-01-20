package kr.lineedu.lms.config.websocket;

import kr.lineedu.lms.config.jwt.JwtPrincipal;
import kr.lineedu.lms.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * WebSocket 연결 및 메시지 전송 시 JWT 토큰을 검증하는 인터셉터
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        
        if (accessor == null) {
            return message;
        }

        // CONNECT 또는 SEND 명령일 때만 인증 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand()) || 
            StompCommand.SEND.equals(accessor.getCommand())) {
            
            // Authorization 헤더에서 토큰 추출
            String token = extractToken(accessor);
            
            if (token == null || token.isEmpty()) {
                log.warn("WebSocket connection rejected: No token provided");
                throw new SecurityException("Authentication token is required");
            }

            try {
                // Redis에서 로그아웃된 토큰인지 확인
                checkTokenFromRedis(token);

                // 토큰 검증
                JwtTokenProvider.JwtCode jwtCode = jwtTokenProvider.validateToken(token);
                
                if (jwtCode == JwtTokenProvider.JwtCode.ACCESS) {
                    // 토큰이 유효하면 인증 정보 설정
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    accessor.setUser(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
                    log.info("WebSocket authenticated user: userId={}, lmsUserId={}", 
                        principal.getUserId(), principal.getLmsUserId());
                } else {
                    log.warn("WebSocket connection rejected: Invalid token - {}", jwtCode);
                    throw new SecurityException("Invalid authentication token");
                }
            } catch (Exception e) {
                log.error("WebSocket authentication failed: {}", e.getMessage());
                throw new SecurityException("Authentication failed: " + e.getMessage());
            }
        }

        return message;
    }

    /**
     * STOMP 헤더에서 JWT 토큰을 추출합니다.
     * Authorization 헤더 또는 token 헤더에서 토큰을 찾습니다.
     */
    private String extractToken(StompHeaderAccessor accessor) {
        // Authorization 헤더 확인 (Bearer token 형식)
        String authHeader = accessor.getFirstNativeHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // token 헤더 확인 (일부 클라이언트에서 사용)
        String tokenHeader = accessor.getFirstNativeHeader("token");
        if (tokenHeader != null && !tokenHeader.isEmpty()) {
            return tokenHeader;
        }

        return null;
    }

    /**
     * Redis에서 로그아웃된 토큰인지 확인합니다.
     */
    private void checkTokenFromRedis(String token) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String value = vop.get(token);
        if (Objects.equals(value, "logout")) {
            throw new SecurityException("Token has been logged out");
        }
    }
}

