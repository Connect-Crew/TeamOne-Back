package com.connectcrew.teamone.compositeservice.global.config;

import com.connectcrew.teamone.compositeservice.global.exception.WebClientExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public WebClientExceptionHandler exHandler() {
        return new WebClientExceptionHandler();
    }
}
