package com.connectcrew.teamone.chatservice.config;

import com.connectcrew.teamone.chatservice.request.ProjectRequest;
import com.connectcrew.teamone.chatservice.request.ProjectRequestImpl;
import com.connectcrew.teamone.chatservice.request.UserRequest;
import com.connectcrew.teamone.chatservice.request.UserRequestImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public UserRequest userRequest() {
        return new UserRequestImpl();
    }

    @Bean
    public ProjectRequest projectRequest() {
        return new ProjectRequestImpl();
    }
}
