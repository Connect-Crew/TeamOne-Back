package com.connectcrew.teamone.projectservice.notification.adapter.out.event;

import com.connectcrew.teamone.projectservice.notification.application.port.out.SendNotificationOutput;
import com.connectcrew.teamone.projectservice.notification.domain.Notification;
import com.connectcrew.teamone.projectservice.notification.domain.constants.KafkaEventTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NotificationEventAdapter implements SendNotificationOutput {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(Notification notification) {
        try {
            String body = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send(KafkaEventTopic.PushNotification, body);
        } catch (Exception e) {
            log.error("send error - topic: {}, body: {}",  KafkaEventTopic.PushNotification, notification , e);
        }
    }
}
