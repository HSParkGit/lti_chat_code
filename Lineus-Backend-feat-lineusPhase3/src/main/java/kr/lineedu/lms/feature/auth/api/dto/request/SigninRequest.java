package kr.lineedu.lms.feature.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {

    @NotBlank(message = "User number is required")
    private String userNumber;

    @NotBlank(message = "Password is required")
    private String password;
}
