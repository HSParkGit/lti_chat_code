package kr.lineedu.lms.feature.sso.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for SSO authentication from LTI
 */
@Getter
@Setter
public class SSOAuthRequest {
    
    @NotNull(message = "Canvas user ID is required")
    private Long canvasUserId; // Canvas LMS user ID
    
    private String email; // Optional: user email from Canvas
    private String name; // Optional: user name from Canvas
    private String loginId; // Optional: login ID from Canvas
    private String role; // Optional: user role (STUDENT, TEACHER, ADMIN)
}

