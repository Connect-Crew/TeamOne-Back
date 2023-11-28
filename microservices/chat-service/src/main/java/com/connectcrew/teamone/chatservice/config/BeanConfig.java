package com.connectcrew.teamone.chatservice.config;

import com.connectcrew.teamone.chatservice.request.ProjectRequest;
import com.connectcrew.teamone.chatservice.request.ProjectRequestImpl;
import com.connectcrew.teamone.chatservice.request.UserRequest;
import com.connectcrew.teamone.chatservice.request.UserRequestImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UserRequest userRequest(@Value("${app.user}") String host, RestTemplate restTemplate) {
        return new UserRequestImpl(host, restTemplate);
    }

    @Bean
    public ProjectRequest projectRequest(@Value("${app.project}") String host, RestTemplate restTemplate) {
        return new ProjectRequestImpl(host, restTemplate);
    }
}
