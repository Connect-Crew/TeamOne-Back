package com.connectcrew.teamone.gateway.config;

import com.connectcrew.teamone.api.user.auth.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .authorizeExchange(spec -> {
                    spec.pathMatchers("/actuator/**").permitAll();
                    spec.pathMatchers("/user/**").hasRole(Role.USER.name());
                    spec.anyExchange().denyAll(); // 기본적인 요청은 모두 거부
                })
                .oauth2Login(withDefaults())
                .build();
    }
}
