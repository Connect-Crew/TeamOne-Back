package com.connectcrew.teamone.compositeservice.config;

import com.connectcrew.teamone.compositeservice.request.ChatRequestImpl;
import com.connectcrew.teamone.compositeservice.request.ProjectRequestImpl;
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
    public UserRequestImpl userRequest(@Value("${app.user}") String host, WebClient webClient) {
        return new UserRequestImpl(host, webClient);
    }

    @Bean
    public ProjectRequestImpl projectRequest(@Value("${app.project}") String host, WebClient webClient) {
        return new ProjectRequestImpl(host, webClient);
    }

    @Bean
    public ChatRequestImpl chatRequest(@Value("${app.chat}") String host, WebClient webClient) {
        return new ChatRequestImpl(host, webClient);
    }
}
