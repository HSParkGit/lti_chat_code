package kr.lineedu.lms.config.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring")
public record ApplicationProperties(
    Redis redis
) {
    public  record Redis(String host, String port, String password) { }
}
