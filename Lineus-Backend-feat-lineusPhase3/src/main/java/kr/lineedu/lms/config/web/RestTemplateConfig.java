package kr.lineedu.lms.config.web;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5)) // 연결 타임아웃 설정 (선택적)
                .setReadTimeout(Duration.ofSeconds(5)) // 읽기 타임아웃 설정 (선택적)
                .build();
    }
}