package net.lomtech.lti.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "lti")
@Getter
@Setter
public class LtiProperties {
    private String baseUrl;
    private String staticRedirectUri;

    /**
     * Gets the redirect URI, computed from baseUrl.
     * LTI redirect URI is typically at {baseUrl}/lti/authorized-redirect
     */
    public String getRedirectUri() {
        if (baseUrl == null || baseUrl.isEmpty()) {
            return null;
        }
        // Remove trailing slash if present
        String url = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return url + "/lti/authorized-redirect";
    }
}


