package kr.lineedu.lms.config.security;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class FilterMain implements Filter {
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws
		IOException,
		ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		log.debug("요청 Url : {}", req.getRequestURL());

		log.debug("Filter 시작");
		chain.doFilter(request, response);
		log.debug("Filter 종료");
	}
}
