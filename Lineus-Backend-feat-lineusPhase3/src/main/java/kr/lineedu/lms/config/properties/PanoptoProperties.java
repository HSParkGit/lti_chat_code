package kr.lineedu.lms.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "panopto")
@Data
public class PanoptoProperties {
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String userName;
    private String password;
    private String instanceName;
}
