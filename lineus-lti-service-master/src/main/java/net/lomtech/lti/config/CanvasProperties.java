package net.lomtech.lti.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "canvas")
@Getter
@Setter
public class CanvasProperties {
    private String issuer;
    private String baseUrl;
    private ClientIds clientIds;

    /**
     * Gets the authorized URL, computed from baseUrl if not explicitly set.
     * Canvas LTI authorization endpoint is typically at /api/lti/authorize
     */
    public String getAuthorizedUrl() {
        if (baseUrl == null || baseUrl.isEmpty()) {
            return null;
        }
        // Remove trailing slash if present
        String url = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return url + "/api/lti/authorize";
    }

    /**
     * Gets the JWKS URI, computed from baseUrl if not explicitly set.
     * Canvas LTI JWKS endpoint is typically at /api/lti/security/jwks
     */
    public String getJwksUri() {
        if (baseUrl == null || baseUrl.isEmpty()) {
            return null;
        }
        // Remove trailing slash if present
        String url = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return url + "/api/lti/security/jwks";
    }

    @Getter
    @Setter
    public static class ClientIds {
        private String chatRoom;
        private String chatRoomDemo;
    }
}


