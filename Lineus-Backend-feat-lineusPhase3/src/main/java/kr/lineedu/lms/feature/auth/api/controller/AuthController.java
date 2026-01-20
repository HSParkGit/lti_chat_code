package kr.lineedu.lms.feature.auth.api.controller;

import jakarta.validation.Valid;
import kr.lineedu.lms.feature.chatSystem.api.dto.request.RefreshAccessTokenRequest;
import kr.lineedu.lms.feature.chatSystem.api.dto.response.RefreshTokenResponse;
import kr.lineedu.lms.feature.auth.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> regenerateAccessToken(
            @Valid @RequestBody RefreshAccessTokenRequest request) {
        log.info("Refresh token request received");
        RefreshTokenResponse response = authService.regenerateAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}

