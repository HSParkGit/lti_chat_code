package kr.lineedu.lms.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignRequestBearerTokenInterceptor implements RequestInterceptor {

	private final String token;

	public FeignRequestBearerTokenInterceptor(final String token) {
		this.token = token;
	}

	@Override
	public void apply(final RequestTemplate template) {
		template.header("Authorization", "Bearer " + token);
	}
}
