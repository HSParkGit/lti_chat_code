package net.lomtech.lti.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "frontend")
@Getter
@Setter
public class FrontendProperties {
    private String baseUrl;

    /**
     * Gets the frontend URL, computed from baseUrl.
     * Frontend URL is typically at {baseUrl}/by_pass_authenticate
     */
    public String getUrl() {
        if (baseUrl == null || baseUrl.isEmpty()) {
            return null;
        }
        // Remove trailing slash if present
        String url = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return url + "/by_pass_authenticate";
    }
}


