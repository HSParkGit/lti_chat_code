package kr.lineedu.lms.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class PanoptoRequestBearerTokenInterceptor implements RequestInterceptor {

	private final String token;

	public PanoptoRequestBearerTokenInterceptor(final String token) {
		this.token = token;
	}

	@Override
	public void apply(final RequestTemplate template) {
		template.header("Authorization", "Bearer " + token);
	}
}
