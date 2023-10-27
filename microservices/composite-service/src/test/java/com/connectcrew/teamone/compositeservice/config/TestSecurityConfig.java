package com.connectcrew.teamone.compositeservice.config;

import com.connectcrew.teamone.compositeservice.auth.JwtAuthenticationManager;
import com.connectcrew.teamone.compositeservice.auth.JwtSecurityContextRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    @MockBean
    JwtAuthenticationManager jwtAuthenticationManager;

    @MockBean
    JwtSecurityContextRepository jwtSecurityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(spec -> spec.anyExchange().permitAll())
                .build();
    }
}