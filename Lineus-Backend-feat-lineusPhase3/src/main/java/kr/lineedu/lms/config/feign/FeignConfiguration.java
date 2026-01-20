package kr.lineedu.lms.config.feign;

import feign.Request;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableFeignClients(basePackages = "kr.lineedu.lms")
@Import(FeignClientsConfiguration.class)
public class FeignConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientExceptionErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 2000, 1);
    }

//    @Bean
//    public Request.Options feignOptions() {
//        return new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, false); // Disable redirects
//    }
}
