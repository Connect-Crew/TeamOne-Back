package com.connectcrew.teamone.compositeservice.global.error.adapter.out;

import com.connectcrew.teamone.compositeservice.global.constants.KafkaEventTopic;
import com.connectcrew.teamone.compositeservice.global.error.application.port.out.SendErrorNotificationOutput;
import com.connectcrew.teamone.compositeservice.global.error.domain.ErrorNotification;
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
