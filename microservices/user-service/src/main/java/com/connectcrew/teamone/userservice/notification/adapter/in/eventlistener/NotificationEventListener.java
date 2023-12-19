package com.connectcrew.teamone.userservice.notification.adapter.in.eventlistener;

import com.connectcrew.teamone.userservice.gloabal.constants.KafkaEventTopic;
import com.connectcrew.teamone.userservice.notification.adapter.in.eventlistener.event.DiscordMessageEvent;
import com.connectcrew.teamone.userservice.notification.adapter.in.eventlistener.event.SendMessageEvent;
import com.connectcrew.teamone.userservice.notification.application.port.in.SendMessageUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final ObjectMapper objectMapper;
    private final SendMessageUseCase sendMessageUseCase;

    @KafkaListener(topics = KafkaEventTopic.PushNotification, groupId = "user-service")
    public void consumePushNotificationEvent(String body) {
        try {
            SendMessageEvent event = objectMapper.readValue(body, SendMessageEvent.class);

            sendMessageUseCase.sendMessage(event.toCommand()).subscribe();
        } catch (Exception e) {
            log.error("consumePushNotificationEvent error", e);
        }
    }

    @KafkaListener(topics = KafkaEventTopic.DiscordNotification, groupId = "user-service")
    public void consumeDiscordNotificationEvent(String body) {
        try {
            DiscordMessageEvent event = objectMapper.readValue(body, DiscordMessageEvent.class);

            sendMessageUseCase.sendMessage(event.toCommand()).subscribe();
        } catch (Exception e) {
            log.error("consumeDiscordNotificationEvent error", e);
        }
    }
}
