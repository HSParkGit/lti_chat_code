package kr.lineedu.lms.feature.sso.api.controller;

import jakarta.validation.Valid;
import kr.lineedu.lms.feature.sso.api.dto.request.SSOAuthRequest;
import kr.lineedu.lms.feature.sso.api.dto.response.SSOAuthResponse;
import kr.lineedu.lms.feature.sso.api.service.SSOService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for SSO authentication endpoints
 * Used for LTI authentication without login page
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sso")
public class SSOController {

    private final SSOService ssoService;

    /**
     * Authenticate user via SSO (from LTI)
     * Accepts Canvas user information and returns JWT tokens
     * 
     * This endpoint is called by the LTI service after validating LTI authentication
     */
    @PostMapping("/authenticate")
    public ResponseEntity<SSOAuthResponse> authenticate(@Valid @RequestBody SSOAuthRequest request) {
        log.info("SSO authenticate endpoint called with canvasUserId: {}, email: {}, name: {}", 
            request.getCanvasUserId(), request.getEmail(), request.getName());
        try {
            SSOAuthResponse response = ssoService.authenticate(request);
            log.info("SSO authenticate successful, returning response with userId: {}", response.getUserId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("SSO authenticate failed in controller: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello, World!");
    }
}

