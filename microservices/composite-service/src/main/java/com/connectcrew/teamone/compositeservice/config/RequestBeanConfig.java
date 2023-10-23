package com.connectcrew.teamone.compositeservice.config;

import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.compositeservice.auth.TokenGenerator;
import com.connectcrew.teamone.compositeservice.request.UserRequest;
import com.connectcrew.teamone.compositeservice.request.UserRequestImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RequestBeanConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public UserRequest userRequest(@Value("${app.user}") String host, WebClient webClient) {
        return new UserRequestImpl(host, webClient);
    }

    @Bean
    public TokenGenerator generator() {
        return new TokenGenerator() { // TODO 임시코드. 향후 제거
            @Override
            public String createToken(String account, Role role) {
                return null;
            }

            @Override
            public String createRefreshToken(String account, Role role) {
                return null;
            }
        };
    }
}
