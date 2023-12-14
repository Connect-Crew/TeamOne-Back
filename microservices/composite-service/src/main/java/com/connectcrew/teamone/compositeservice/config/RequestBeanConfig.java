package com.connectcrew.teamone.compositeservice.config;

import com.connectcrew.teamone.compositeservice.global.exception.WebClientExceptionHandler;
import com.connectcrew.teamone.compositeservice.request.ProjectRequestImpl;
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
    public WebClientExceptionHandler exHandler() {
        return new WebClientExceptionHandler();
    }

    @Bean
    public ProjectRequestImpl projectRequest(@Value("${app.project}") String host, WebClient webClient) {
        return new ProjectRequestImpl(host, webClient);
    }
}
