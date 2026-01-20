package kr.lineedu.lms.config.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public record AppProperties(
    File file,
    Canvas canvas,
    Panopto panopto,
    Jwt jwt,
    Util util,
    Cron cron

) {

    public record File(String dir, String baseUrl){}

    public record Canvas(String masterToken){}

    public record Panopto(String baseUrl, String userName, String password, String clientId, String clientSecret){}

    public record Jwt(String secret, long accessTokenTime,  long refreshTokenTime, String issuer){}

    public record Util(String serviceUrl, String canvasUrl, String portalUrl, String panoptoBase, String masterPassword, String initPassword, Long roleNumber){}

    public record Cron(String instance){}

}
