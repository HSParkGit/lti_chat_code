package kr.lineedu.lms.feature.auth.api.service;

import io.jsonwebtoken.Claims;
import kr.lineedu.lms.config.jwt.JwtTokenProvider;
import kr.lineedu.lms.feature.auth.api.dto.request.SigninRequest;
import kr.lineedu.lms.feature.auth.api.dto.response.SigninResponse;
import kr.lineedu.lms.feature.chatSystem.api.dto.response.RefreshTokenResponse;
import kr.lineedu.lms.feature.user.domain.User;
import kr.lineedu.lms.feature.user.domain.UserRepository;
import kr.lineedu.lms.global.enums.Role;
import kr.lineedu.lms.global.error.dto.ErrorCode;
import kr.lineedu.lms.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public SigninResponse signin(SigninRequest request) {
        log.info("Signin attempt for user: {}", request.getUserNumber());

        // Find user by loginId
        User user = userRepository.findByLoginId(request.getUserNumber())
            .orElseThrow(() -> {
                log.warn("User not found: {}", request.getUserNumber());
                return new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION, "Invalid credentials");
            });

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user: {}", request.getUserNumber());
            throw new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION, "Invalid credentials");
        }

        // Get role
        Role role = Role.toEnum(user.getRole());
        if (role == null) {
            role = Role.STUDENT;
        }

        // Generate tokens
        String accessToken = jwtTokenProvider.createAccessToken(user, role, user.getLoginId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user, role);

        log.info("Signin successful for user: {}", request.getUserNumber());

        return SigninResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .lmsUserId(user.getLmsUserId())
            .userId(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .role(role.name())
            .build();
    }

    public RefreshTokenResponse regenerateAccessToken(String refreshToken) {
        // Validate refresh token from Redis
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String tokenValue = vop.get(refreshToken);
        
        if (tokenValue == null || !tokenValue.equals("refreshtoken")) {
            log.warn("Invalid or expired refresh token");
            throw new BusinessException(ErrorCode.NOT_FOUND_REFRESH_TOKEN, 
                "Invalid or expired refresh token");
        }
        
        // Parse refresh token to get user info
        Claims claims = jwtTokenProvider.parseToken(refreshToken);
        String loginId = claims.get("loginId", String.class);
        String roleStr = claims.get("role", String.class);
        Role role = Role.valueOf(roleStr);
        
        // Get user from repository
        User user = userRepository.findByLoginId(loginId)
            .orElseThrow(() -> {
                log.warn("User not found for loginId: {}", loginId);
                return new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION, 
                    "User not found: " + loginId);
            });
        
        // Generate new access token
        String newAccessToken = jwtTokenProvider.createAccessToken(user, role, loginId);
        
        // Generate new refresh token and remove old one
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user, role);
        redisTemplate.delete(refreshToken);
        
        log.info("Access token regenerated successfully for user: {}", loginId);
        
        return RefreshTokenResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .tokenType("Bearer")
            .build();
    }
}

