package kr.lineedu.lms.feature.auth.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigninResponse {
    private String accessToken;
    private String refreshToken;
    private Long lmsUserId;
    private Long userId;
    private String name;
    private String email;
    private String role;
}
