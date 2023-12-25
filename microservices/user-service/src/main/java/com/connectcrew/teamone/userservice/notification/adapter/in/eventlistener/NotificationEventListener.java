package com.connectcrew.teamone.userservice.notification.adapter.in.eventlistener;

import com.connectcrew.teamone.api.constants.KafkaEventTopic;
import com.connectcrew.teamone.api.userservice.notification.push.SendMessageEvent;
import com.connectcrew.teamone.api.userservice.notification.error.ErrorNotification;
import com.connectcrew.teamone.userservice.notification.application.port.in.SendMessageUseCase;
import com.connectcrew.teamone.userservice.notification.application.port.in.command.DiscordMessageCommand;
import com.connectcrew.teamone.userservice.notification.application.port.in.command.SendMessageCommand;
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

            sendMessageUseCase.sendMessage(SendMessageCommand.from(event)).subscribe();
        } catch (Exception e) {
            log.error("consumePushNotificationEvent error", e);
        }
    }

    @KafkaListener(topics = KafkaEventTopic.ErrorNotification, groupId = "user-service")
    public void consumeErrorNotificationEvent(String body) {
        try {
            ErrorNotification event = objectMapper.readValue(body, ErrorNotification.class);

            sendMessageUseCase.sendMessage(DiscordMessageCommand.from(event)).subscribe();
        } catch (Exception e) {
            log.error("consumeErrorNotificationEvent error", e);
        }
    }
}
