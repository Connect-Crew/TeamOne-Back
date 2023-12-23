package com.connectcrew.teamone.projectservice.global.exceptions.adapter;

import com.connectcrew.teamone.projectservice.global.constants.KafkaEventTopic;
import com.connectcrew.teamone.projectservice.global.exceptions.application.port.out.SendErrorNotificationOutput;
import com.connectcrew.teamone.projectservice.global.exceptions.domain.ErrorNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ErrorNotificationPublisher implements SendErrorNotificationOutput {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(ErrorNotification notification) {
        try {
            String body = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send(KafkaEventTopic.ErrorNotification, body);
        } catch (Exception e) {
            log.error("send error - topic: {}, body: {}",  KafkaEventTopic.ErrorNotification, notification , e);
        }
    }
}
