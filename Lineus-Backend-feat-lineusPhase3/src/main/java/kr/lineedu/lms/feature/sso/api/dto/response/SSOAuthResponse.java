package kr.lineedu.lms.feature.sso.api.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * Response DTO for SSO authentication
 */
@Getter
@Builder
public class SSOAuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long userId; // Local user ID
    private Long lmsUserId; // Canvas user ID
}

