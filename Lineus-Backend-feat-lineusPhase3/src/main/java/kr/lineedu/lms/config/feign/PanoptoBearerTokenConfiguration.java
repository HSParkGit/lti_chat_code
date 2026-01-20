package kr.lineedu.lms.config.feign;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

@ConditionalOnBean(name = "panoptoAccessToken")
public class PanoptoBearerTokenConfiguration {

	@Bean
	@ConditionalOnBean(name = "panoptoAccessToken")
	public RequestInterceptor panoptoRequestInterceptor(Object panoptoAccessToken) {
		// This will only be created if PanoptoAccessToken bean exists
		// Using Object type to avoid compilation error when class doesn't exist
		return new PanoptoRequestBearerTokenInterceptor("");
	}
}
