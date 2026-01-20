package kr.lineedu.lms.feature.sso.api.service;

import kr.lineedu.lms.config.jwt.JwtTokenProvider;
import kr.lineedu.lms.feature.chatSystem.api.feign.CanvasChatClient;
import kr.lineedu.lms.feature.chatSystem.api.dto.feign.CanvasUserDto;
import kr.lineedu.lms.feature.sso.api.dto.request.SSOAuthRequest;
import kr.lineedu.lms.feature.sso.api.dto.response.SSOAuthResponse;
import kr.lineedu.lms.feature.user.domain.User;
import kr.lineedu.lms.feature.user.domain.UserRepository;
import kr.lineedu.lms.global.enums.Role;
import kr.lineedu.lms.global.enums.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import feign.FeignException;

import java.util.Optional;

/**
 * Service for handling SSO authentication from LTI
 * Creates or finds users and generates JWT tokens
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SSOService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CanvasChatClient canvasChatClient;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticate user via SSO (from LTI)
     * Finds or creates user based on Canvas user ID and generates JWT tokens
     */
    public SSOAuthResponse authenticate(SSOAuthRequest request) {
        log.info("SSO authentication request for Canvas user ID: {}", request.getCanvasUserId());

        try {
            // Try to find existing user by Canvas LMS user ID
            Optional<User> existingUserOpt = userRepository.findByLmsUserId(request.getCanvasUserId());

            User user;
            if (existingUserOpt.isPresent()) {
                user = existingUserOpt.get();
                log.info("Found existing user: userId={}, lmsUserId={}", user.getId(), user.getLmsUserId());
            } else {
                // User doesn't exist, fetch from Canvas and create
                log.info("User not found, fetching from Canvas and creating new user");
                user = createUserFromCanvas(request);
            }

            // Determine role
            Role userRole = determineRole(request.getRole(), user);
            
            // Validate and set required fields before token generation
            String userEmail = user.getEmail();
            if (userEmail == null || userEmail.isEmpty()) {
                log.warn("User email is null or empty, using fallback email for user: userId={}, lmsUserId={}", 
                    user.getId(), user.getLmsUserId());
                userEmail = request.getEmail() != null && !request.getEmail().isEmpty() 
                    ? request.getEmail() 
                    : "user" + request.getCanvasUserId() + "@canvas.local";
                user.setEmail(userEmail);
            }
            
            // Ensure loginId is set
            if (user.getLoginId() == null || user.getLoginId().isEmpty()) {
                String loginId = request.getLoginId() != null && !request.getLoginId().isEmpty()
                    ? request.getLoginId()
                    : String.valueOf(request.getCanvasUserId());
                user.setLoginId(loginId);
            }
            
            // Ensure division and subDivision are not null (JWT builder may fail with null)
            if (user.getDivision() == null) {
                user.setDivision("");
            }
            if (user.getSubDivision() == null) {
                user.setSubDivision("");
            }
            
            // Ensure name is set
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(request.getName() != null ? request.getName() : "LTI User");
            }
            
            // Save user if any fields were updated
            user = userRepository.save(user);
            
            // Double-check all required fields before token generation
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                throw new IllegalStateException("User email cannot be null or empty after validation");
            }
            if (user.getLoginId() == null || user.getLoginId().isEmpty()) {
                throw new IllegalStateException("User loginId cannot be null or empty after validation");
            }
            
            String loginId = user.getLoginId();
            log.info("Generating tokens for user: userId={}, loginId={}, email={}, role={}, division={}, subDivision={}", 
                user.getId(), loginId, user.getEmail(), userRole, user.getDivision(), user.getSubDivision());
            
            try {
                String accessToken = jwtTokenProvider.createAccessToken(user, userRole, loginId);
                String refreshToken = jwtTokenProvider.createRefreshToken(user, userRole);
                
                if (accessToken == null || accessToken.isEmpty()) {
                    log.error("Failed to generate access token for user: userId={}, lmsUserId={}", user.getId(), user.getLmsUserId());
                    throw new RuntimeException("Failed to generate access token");
                }
                
                if (refreshToken == null || refreshToken.isEmpty()) {
                    log.error("Failed to generate refresh token for user: userId={}, lmsUserId={}", user.getId(), user.getLmsUserId());
                    throw new RuntimeException("Failed to generate refresh token");
                }
                
                log.info("SSO authentication successful: userId={}, lmsUserId={}, role={}", 
                    user.getId(), user.getLmsUserId(), userRole);

                return SSOAuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .userId(user.getId())
                    .lmsUserId(user.getLmsUserId())
                    .build();
            } catch (Exception tokenException) {
                log.error("Token generation failed for user: userId={}, lmsUserId={}, error={}", 
                    user.getId(), user.getLmsUserId(), tokenException.getMessage(), tokenException);
                throw new RuntimeException("Token generation failed: " + tokenException.getMessage(), tokenException);
            }
        } catch (Exception e) {
            log.error("SSO authentication failed for Canvas user ID {}: {}", request.getCanvasUserId(), e.getMessage(), e);
            log.error("Exception type: {}, Stack trace: ", e.getClass().getName(), e);
            throw new RuntimeException("SSO authentication failed: " + e.getMessage(), e);
        }
    }

    /**
     * Create a new user from Canvas information
     */
    private User createUserFromCanvas(SSOAuthRequest request) {
        CanvasUserDto canvasUser = null;
        try {
            log.info("Attempting to fetch user from Canvas API for user ID: {}", request.getCanvasUserId());
            // Fetch user details from Canvas
            try {
                canvasUser = canvasChatClient.getUserById(request.getCanvasUserId(), null);
            } catch (feign.FeignException fe) {
                log.error("FeignException when calling Canvas API for user ID {}: status={}, message={}", 
                    request.getCanvasUserId(), fe.status(), fe.getMessage(), fe);
                // Fall through to create user with minimal info
                canvasUser = null;
            } catch (Exception apiEx) {
                log.error("Exception when calling Canvas API for user ID {}: type={}, message={}", 
                    request.getCanvasUserId(), apiEx.getClass().getName(), apiEx.getMessage(), apiEx);
                // Fall through to create user with minimal info
                canvasUser = null;
            }
            
            // Build user entity with email validation
            // Use Canvas data if available, otherwise use request data
            String userEmail;
            String loginId;
            String userName;
            
            if (canvasUser != null) {
                log.info("Successfully fetched user from Canvas: name={}, email={}, loginId={}", 
                    canvasUser.getName(), canvasUser.getEmail(), canvasUser.getLoginId());
                
                userEmail = canvasUser.getEmail() != null && !canvasUser.getEmail().isEmpty()
                    ? canvasUser.getEmail()
                    : (request.getEmail() != null && !request.getEmail().isEmpty()
                        ? request.getEmail()
                        : "user" + request.getCanvasUserId() + "@canvas.local");
                
                loginId = canvasUser.getLoginId() != null && !canvasUser.getLoginId().isEmpty()
                    ? canvasUser.getLoginId()
                    : (request.getLoginId() != null && !request.getLoginId().isEmpty()
                        ? request.getLoginId()
                        : String.valueOf(request.getCanvasUserId()));
                
                userName = canvasUser.getName() != null && !canvasUser.getName().isEmpty()
                    ? canvasUser.getName()
                    : (request.getName() != null && !request.getName().isEmpty()
                        ? request.getName()
                        : "LTI User");
            } else {
                log.warn("Canvas API returned null or failed for user ID: {}, using request data to create user", request.getCanvasUserId());
                // Use request data directly
                userEmail = request.getEmail() != null && !request.getEmail().isEmpty() 
                    ? request.getEmail() 
                    : "user" + request.getCanvasUserId() + "@canvas.local";
                
                loginId = request.getLoginId() != null && !request.getLoginId().isEmpty()
                    ? request.getLoginId()
                    : String.valueOf(request.getCanvasUserId());
                
                userName = request.getName() != null && !request.getName().isEmpty()
                    ? request.getName()
                    : "LTI User";
            }
            
            log.info("Creating user with: email={}, loginId={}, name={}", userEmail, loginId, userName);
            
            User newUser = User.builder()
                .lmsUserId(request.getCanvasUserId())
                .loginId(loginId)
                .name(userName)
                .email(userEmail)
                .password(passwordEncoder.encode("lti_user_" + request.getCanvasUserId())) // Temporary password
                .state(State.ACTIVE.getNumber())
                .role(determineRole(request.getRole(), null).getNumber())
                .division("") // Ensure division is not null
                .subDivision("") // Ensure subDivision is not null
                .build();

            User savedUser = userRepository.save(newUser);
            log.info("Created new user from Canvas: userId={}, lmsUserId={}, email={}, loginId={}", 
                savedUser.getId(), savedUser.getLmsUserId(), savedUser.getEmail(), savedUser.getLoginId());
            return savedUser;
        } catch (Exception e) {
            log.error("Failed to fetch user from Canvas (exception type: {}), creating with minimal info: {}", 
                e.getClass().getName(), e.getMessage(), e);
            
            // Fallback: create user with minimal information
            String fallbackEmail = request.getEmail() != null && !request.getEmail().isEmpty() 
                ? request.getEmail() 
                : "user" + request.getCanvasUserId() + "@canvas.local";
            
            String fallbackLoginId = request.getLoginId() != null && !request.getLoginId().isEmpty()
                ? request.getLoginId()
                : String.valueOf(request.getCanvasUserId());
            
            String fallbackName = request.getName() != null && !request.getName().isEmpty()
                ? request.getName()
                : "LTI User";
            
            log.info("Creating user with fallback info: email={}, loginId={}, name={}", 
                fallbackEmail, fallbackLoginId, fallbackName);
            
            User newUser = User.builder()
                .lmsUserId(request.getCanvasUserId())
                .loginId(fallbackLoginId)
                .name(fallbackName)
                .email(fallbackEmail)
                .password(passwordEncoder.encode("lti_user_" + request.getCanvasUserId()))
                .state(State.ACTIVE.getNumber())
                .role(determineRole(request.getRole(), null).getNumber())
                .division("") // Ensure division is not null
                .subDivision("") // Ensure subDivision is not null
                .build();

            User savedUser = userRepository.save(newUser);
            log.info("Created new user with fallback info: userId={}, lmsUserId={}, email={}, loginId={}", 
                savedUser.getId(), savedUser.getLmsUserId(), savedUser.getEmail(), savedUser.getLoginId());
            return savedUser;
        }
    }

    /**
     * Determine user role from request or default to STUDENT
     */
    private Role determineRole(String roleString, User user) {
        if (roleString != null && !roleString.isEmpty()) {
            try {
                Role role = Role.fromString(roleString.toUpperCase());
                if (role != null) {
                    return role;
                }
            } catch (Exception e) {
                log.warn("Invalid role string: {}, defaulting to STUDENT", roleString);
            }
        }
        
        // If user exists, use their existing role
        if (user != null && user.getRole() != null) {
            Role role = Role.toEnum(user.getRole());
            if (role != null) {
                return role;
            }
        }
        
        // Default to STUDENT
        return Role.STUDENT;
    }
}

