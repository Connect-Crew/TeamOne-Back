package com.connectcrew.teamone.compositeservice.config;

import com.connectcrew.teamone.compositeservice.auth.application.Auth2TokenValidator;
import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.composite.adapter.out.web.ChatWebAdapter;
import com.connectcrew.teamone.compositeservice.composite.adapter.out.web.ProjectWebAdapter;
import com.connectcrew.teamone.compositeservice.composite.adapter.out.web.UserWebAdapter;
import com.connectcrew.teamone.compositeservice.file.adapter.out.file.StaticFileAdapter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;

@TestConfiguration
public class TestBeanConfig {
    @MockBean
    KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    ProducerFactory<String, String> producerFactory;

    @MockBean
    ConsumerFactory<String, String> consumerFactory;

    @MockBean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory;

    @MockBean
    MessageListenerContainer messageListenerContainer;

    @MockBean
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @MockBean
    private Auth2TokenValidator tokenValidator;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    ChatWebAdapter chatWebAdapter;

    @MockBean
    ProjectWebAdapter projectWebAdapter;

    @MockBean
    UserWebAdapter userWebAdapter;

    @MockBean
    StaticFileAdapter staticFileAdapter;
}
