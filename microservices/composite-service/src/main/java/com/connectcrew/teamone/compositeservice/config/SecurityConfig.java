package com.connectcrew.teamone.compositeservice.config;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    private final ReactiveAuthenticationManager authenticationManager;

    private final ServerSecurityContextRepository securityContextRepository;

    public SecurityConfig(ReactiveAuthenticationManager authenticationManager, ServerSecurityContextRepository securityContextRepository) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .exceptionHandling(spec -> {
                    spec.accessDeniedHandler((exchange, accessDeniedException) -> handleAccessDenied(exchange));
                    spec.authenticationEntryPoint(((exchange, authException) -> handleTokenExpired(exchange)));
                })
                .authorizeExchange(spec -> {
                    spec.pathMatchers("/auth/login", "/auth/register", "/actuator/**", "/project/", "/project/list").permitAll();
                    spec.anyExchange().denyAll();
                })
                .build();
    }

    private Mono<Void> handleTokenExpired(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.UNAUTHORIZED, exchange.getRequest().getPath().value(), "인증이 만료되었습니다.");
        byte[] errorBytes;
        try {
            errorBytes = objectMapper.writeValueAsBytes(errorInfo);
        } catch (JsonProcessingException e) {
            errorBytes = "{\"message\": \"Error occurred while processing the response\"}".getBytes(StandardCharsets.UTF_8);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        DataBuffer buffer = response.bufferFactory().wrap(errorBytes);
        return response.writeWith(Mono.just(buffer));
    }

    private Mono<Void> handleAccessDenied(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.UNAUTHORIZED, exchange.getRequest().getPath().value(), "권한이 없는 사용자입니다.");

        byte[] errorBytes;
        try {
            errorBytes = objectMapper.writeValueAsBytes(errorInfo);
        } catch (JsonProcessingException e) {
            errorBytes = "{\"message\": \"Error occurred while processing the response\"}".getBytes(StandardCharsets.UTF_8);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        DataBuffer buffer = response.bufferFactory().wrap(errorBytes);
        return response.writeWith(Mono.just(buffer));
    }
}
