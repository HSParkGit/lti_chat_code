package net.lomtech.lti.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.lomtech.lti.config.AppInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for serving LTI tool configuration JSON.
 * This endpoint is used by LMS systems (like Canvas) to auto-configure LTI tools.
 */
@RestController
public class LTIToolConfigurationController {
    private static final Logger log = LoggerFactory.getLogger(LTIToolConfigurationController.class);
    
    private final AppInfo appInfo;

    public LTIToolConfigurationController(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @GetMapping(value = "/.well-known/lti-tool-configuration.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LTIToolConfiguration> getToolConfiguration() {
        log.info("LTI tool configuration requested");
        
        String baseUrl = appInfo.getLtiBaseUrl();
        
        LTIToolConfiguration config = new LTIToolConfiguration();
        config.setTitle("Lineus LTI Tool");
        config.setDescription("Lineus Learning Tools Interoperability (LTI) Tool");
        config.setTargetLinkUri(baseUrl + "/lti/launch");
        config.setOidcInitiationUrl(baseUrl + "/lti/login-initiation");
        
        // LTI 1.3 scopes (Canvas requires result.readonly, not result)
        List<String> scopes = new ArrayList<>();
        scopes.add("https://purl.imsglobal.org/spec/lti-ags/scope/lineitem");
        scopes.add("https://purl.imsglobal.org/spec/lti-ags/scope/result.readonly");
        scopes.add("https://purl.imsglobal.org/spec/lti-ags/scope/score");
        scopes.add("https://purl.imsglobal.org/spec/lti-nrps/scope/contextmembership.readonly");
        config.setScopes(scopes);
        
        // Public JWK URL (required by Canvas)
        config.setPublicJwkUrl(baseUrl + "/.well-known/jwks.json");
        
        // Canvas-specific extensions (must be an array according to Canvas docs)
        Map<String, Object> canvasExtension = new HashMap<>();
        canvasExtension.put("domain", getDomainFromBaseUrl(baseUrl));
        canvasExtension.put("tool_id", "lineus-lti-tool");
        canvasExtension.put("platform", "canvas.instructure.com");
        canvasExtension.put("privacy_level", "public");
        
        // Settings with placements
        Map<String, Object> settings = new HashMap<>();
        settings.put("text", "Lineus LTI Tool");
        settings.put("icon_url", baseUrl + "/get-image/chatroom-demo.png");
        
        // Placements array
        List<Map<String, Object>> placements = new ArrayList<>();
        Map<String, Object> courseNavPlacement = new HashMap<>();
        courseNavPlacement.put("placement", "course_navigation");
        courseNavPlacement.put("message_type", "LtiResourceLinkRequest");
        courseNavPlacement.put("target_link_uri", baseUrl + "/lti/launch");
        courseNavPlacement.put("text", "Lineus");
        courseNavPlacement.put("icon_url", baseUrl + "/get-image/chatroom-demo.png");
        courseNavPlacement.put("custom_fields", Map.of(
            "canvas_user_id", "$Canvas.user.id",
            "canvas_course_id", "$Canvas.course.id",
            "canvas_user_email", "$Canvas.user.email",
            "custom_person_profile_url","Canvas.user.url"
        ));
        placements.add(courseNavPlacement);
        
        settings.put("placements", placements);
        canvasExtension.put("settings", settings);
        
        // Extensions must be an array
        List<Map<String, Object>> extensions = new ArrayList<>();
        extensions.add(canvasExtension);
        config.setExtensions(extensions);
        
        log.info("Returning LTI tool configuration for base URL: {}", baseUrl);
        return ResponseEntity.ok(config);
    }
    
    private String getDomainFromBaseUrl(String baseUrl) {
        try {
            URI uri = URI.create(baseUrl);
            return uri.getHost();
        } catch (Exception e) {
            log.error("Error extracting domain from base URL: {}", baseUrl, e);
            // Fallback: extract domain from URL
            String domain = baseUrl.replaceFirst("^https?://", "").split("/")[0];
            return domain;
        }
    }
    
    @Data
    public static class LTIToolConfiguration {
        private String title;
        private String description;
        
        @JsonProperty("target_link_uri")
        private String targetLinkUri;
        
        @JsonProperty("oidc_initiation_url")
        private String oidcInitiationUrl;
        
        @JsonProperty("public_jwk_url")
        private String publicJwkUrl;
        
        private List<String> scopes;
        private List<Map<String, Object>> extensions;
    }
}

