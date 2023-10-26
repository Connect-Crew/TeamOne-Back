package com.connectcrew.teamone.compositeservice.config;

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
}
