package kr.lineedu.lms.config.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final HandlerExceptionResolver resolver;

	public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	//스프링 시큐리티의 인증 관련 예외를 처리
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		// Get exception from request attribute if set by JwtFilter, otherwise use authException
		Exception exception = (Exception) request.getAttribute("exception");
		if (exception == null) {
			exception = authException;
		}
		resolver.resolveException(request, response, null, exception);
	}
}
