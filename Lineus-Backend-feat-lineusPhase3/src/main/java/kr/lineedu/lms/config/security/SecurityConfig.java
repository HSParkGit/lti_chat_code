package kr.lineedu.lms.config.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import kr.lineedu.lms.config.jwt.JwtFilterConfigurer;
import kr.lineedu.lms.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
// 메소드 단에서 @PreAuthorize와 @Secured 사용하기 위한 설정
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationEntryPoint entryPoint;
    private final RedisTemplate redisTemplate;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // token을 사용하는 방식이기 때문에 csrf 기능을 꺼줍니다
            .csrf(AbstractHttpConfigurer::disable)
            .cors(c -> c.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authz -> authz
                // Public endpoints (no authentication required)
                .requestMatchers("/error").permitAll() // Spring Boot error endpoint
                .requestMatchers("/actuator/**").permitAll() // Spring Boot actuator for monitoring
                .requestMatchers("/api/v1/sso/**").permitAll() // SSO endpoint for LTI authentication
                .requestMatchers("/api/v1/signin").permitAll() // Login endpoint to get tokens
                .requestMatchers("/api/v1/signup").permitAll() // User registration
                .requestMatchers("/api/v2/users/create").permitAll() // User creation
                .requestMatchers("/api/v1/reissue").permitAll() // Token refresh (needs refresh token)
                .requestMatchers("/api/v1/auth/refresh").permitAll() // Token refresh endpoint
                .requestMatchers("/api/v1/health").permitAll() // Health check endpoint
                .requestMatchers("/api/v1/saml/**").permitAll() // SAML authentication endpoints
                // WebSocket endpoint - HTTP level is public, but authentication is handled by WebSocketAuthInterceptor
                .requestMatchers("/ws/**").permitAll()
                
                // All other endpoints require authentication
                // Role-based access control
                .requestMatchers("/api/v1/chat/**").hasAnyAuthority("STUDENT", "TEACHER", "ADMIN") // Chat endpoints are public
                .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/teach/**").hasAnyAuthority("TEACHER", "ADMIN")
                
                // All /api/v1/** endpoints require authentication (STUDENT, TEACHER, or ADMIN)
                // This includes:
                // - /api/v1/chat/** (all chat endpoints)
                // - /api/v1/logout (requires token to know which token to invalidate)
                // - /api/v1/validate/** (requires token to validate)
                // - /api/v1/mongo/** (requires token)
                .requestMatchers("/api/v1/**").hasAnyAuthority("STUDENT", "TEACHER", "ADMIN")
                
                // Documentation can be public or require auth - keeping public for now
                .requestMatchers("/docs/**").permitAll()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            // 세션을 사용하지 않기 때문에 STATELESS로 설정
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
            .apply(new JwtFilterConfigurer(jwtTokenProvider, redisTemplate));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(
            List.of(
                "https://demo.metacubit.kr",
                "https://dev.metacubit.kr",
                "http://localhost:3000",
                "http://localhost:3001",
                "https://nlms.hanil.ac.kr",
                "https://lms.kumoh.ac.kr",
                "https://jiu-study.ac.kr",
                "http://10.220.190.235:3000",
                "https://lineus-fe.lomtech.net",
                "https://linus-stg.lomtech.net",
                "http://127.0.0.1:5500",
                "http://localhost:5500"
            ));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
