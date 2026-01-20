package kr.lineedu.lms.config.jwt;

import java.io.IOException;
import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.lineedu.lms.global.error.dto.ErrorCode;
import kr.lineedu.lms.global.error.exception.BusinessException;
import kr.lineedu.lms.global.error.exception.OnlyRefreshTokenException;
import kr.lineedu.lms.global.error.exception.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Skip JWT filtering for public endpoints
        return path.startsWith("/actuator") ||
               path.startsWith("/api/v1/sso") ||
               path.startsWith("/api/v1/signin") ||
               path.startsWith("/api/v1/signup") ||
               path.startsWith("/api/v2/users/create") ||
               path.startsWith("/api/v1/reissue") ||
               path.startsWith("/api/v1/auth/refresh") ||
               path.startsWith("/api/v1/health") ||
               path.startsWith("/api/v1/saml") ||
               path.startsWith("/ws/") ||
               path.startsWith("/docs") ||
               path.equals("/error");
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
        final FilterChain filterChain) throws ServletException, IOException {

        // 0. 평소에는 AccessToken 하나만 받는다
        // 1. AccessToken이 만료되면 클라이언트에게 알려준다
        // 2. AccessToken이 만료되면 reissue API로 AccessToken, RefreshToken 둘다 받아서 RefreshToken으로 검증한다
        // 3. 검증 후에 두 토큰 모두 재발행하고, 기존 RefreshToken은 Redis에서 삭제한다

        String refreshToken = resolveToken(request, REFRESH_HEADER);
        String accessToken = resolveToken(request, AUTHORIZATION_HEADER);

        // AccessToken만 들어올 때
        if (accessToken != null && refreshToken == null) {
            try {
                //로그아웃 유저인지 체크
                checkTokenFromRedis(accessToken);

                // AccessToken을 검증해서 상태를 가져온다
                JwtTokenProvider.JwtCode jwtCode = validateToken(accessToken);

                // 그 상태로 Switch문을 돌면서 처리
                switch (jwtCode) {
                    case ACCESS -> {
                        try {
                            setSecurityContextHolderAuthenticationBy(accessToken);
                        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
                            // User not found - token is valid but user doesn't exist
                            log.warn("User not found during JWT authentication: {}", e.getMessage());
                            request.setAttribute("exception", e);
                        }
                    }
                    case EXPIRED -> throw new TokenExpiredException(); // 200
                    default -> throw new JwtException("유효하지 않은 토큰 입니다");
                }
            } catch (Exception e) {
                request.setAttribute("exception", e);
            }

        }
        // RefreshToken만 들어온 경우 에러 처리
        else if (accessToken == null && refreshToken != null) {
            request.setAttribute("exception", new OnlyRefreshTokenException("only refreshToken come"));
        }

        filterChain.doFilter(request, response);
    }

    private void setSecurityContextHolderAuthenticationBy(String jwt) {
        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private JwtTokenProvider.JwtCode validateToken(String jwt) {
        return jwtTokenProvider.validateToken(jwt);
    }

    private void checkTokenFromRedis(String jwt) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        if (jwt != null) {
            String value = vop.get(jwt);
            if (Objects.equals(value, "logout")) {
                throw new BusinessException(ErrorCode.LOGOUT_USER_ACCESS_TOKEN);
            }
        }
    }

    private String resolveToken(HttpServletRequest request, String header) {

        String bearerToken = request.getHeader(header);

        if (Objects.equals(header, AUTHORIZATION_HEADER) && bearerToken != null) {
            // Expect format: "Bearer <token>"
            String trimmed = bearerToken.trim();
            if (trimmed.length() > 7 && trimmed.regionMatches(true, 0, "Bearer ", 0, 7)) {
                return trimmed.substring(7).trim();
            }
            // Invalid bearer format; treat as absent to avoid substring errors
            return null;
        }
        return bearerToken;

    }

}
