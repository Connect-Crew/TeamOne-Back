package com.connectcrew.teamone.chatservice.chat.adapter.out.event;

import com.connectcrew.teamone.chatservice.chat.adapter.out.event.event.ChatSendEvent;
import com.connectcrew.teamone.chatservice.chat.application.port.out.FcmOutput;
import com.connectcrew.teamone.chatservice.chat.domain.Chat;
import com.connectcrew.teamone.chatservice.global.constants.KafkaEventTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements FcmOutput {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Override
    public void publish(Chat chat, Set<Long> users) {
        try {
            ChatSendEvent event = new ChatSendEvent(
                    chat.type(),
                    chat.sender(),
                    chat.roomId(),
                    users,
                    chat.content(),
                    chat.timestamp()
            );

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(KafkaEventTopic.SendChat, message);
        } catch (Exception e) {
            log.error("publish error", e);
        }

    }
}
