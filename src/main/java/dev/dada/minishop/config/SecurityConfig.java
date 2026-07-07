package dev.dada.minishop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * TASK MS-11: Cau hinh Spring Security filter chain.
 * - Stateless session, tat csrf
 * - Public: /api/auth/**, /swagger-ui/**, GET /api/products/**
 * - Cac endpoint /api/admin/** yeu cau role ADMIN
 * - Gan JwtAuthenticationFilter truoc UsernamePasswordAuthenticationFilter
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO MS-11: cau hinh chain
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    // TODO MS-11: @Bean PasswordEncoder (BCryptPasswordEncoder)
    // TODO MS-11: @Bean AuthenticationManager
}
