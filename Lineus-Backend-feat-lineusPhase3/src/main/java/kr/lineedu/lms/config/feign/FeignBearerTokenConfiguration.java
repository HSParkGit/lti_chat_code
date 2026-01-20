package kr.lineedu.lms.config.feign;

import kr.lineedu.lms.config.properties.AppProperties;
import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;

public class FeignBearerTokenConfiguration {

    private String token;

    public FeignBearerTokenConfiguration(AppProperties appProperties) {
        this.token = appProperties.canvas().masterToken();
    }

    @Bean
    public RequestInterceptor bearerTokenRequestHeader() {
        return new FeignRequestBearerTokenInterceptor(token);
    }
}
