package kr.lineedu.lms.config.jpa;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import kr.lineedu.lms.config.jwt.JwtPrincipal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableJpaAuditing
@Configuration
public class JpaConfig {

	@Bean
	public AuditorAware<String> auditorAware() {
		return () -> Optional.ofNullable(SecurityContextHolder.getContext())
			.map(SecurityContext::getAuthentication)
			.filter(Authentication::isAuthenticated)
			.map(Authentication::getPrincipal)
			.filter(JwtPrincipal.class::isInstance)
			.map(JwtPrincipal.class::cast)
			.map(JwtPrincipal::getUser);
	}
}
