package kr.lineedu.lms.config.feign;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class DisableRedirect {

    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, false); // Disable redirects
    }
}
