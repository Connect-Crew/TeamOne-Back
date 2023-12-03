package com.connectcrew.teamone.chatservice.chatroom.adapter.in.event;

import com.connectcrew.teamone.chatservice.chatroom.application.port.in.UpdateChatRoomUseCase;
import com.connectcrew.teamone.chatservice.global.constants.KafkaEventTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberModifiedEventListener {
    private final ObjectMapper objectMapper;
    private final UpdateChatRoomUseCase updateChatRoomUseCase;

    @KafkaListener(topics = KafkaEventTopic.MemberModified, groupId = "chat-service")
    public void consumeMemberModifiedEvent(String body) {
        try {
            MemberModifiedEvent event = objectMapper.readValue(body, MemberModifiedEvent.class);

            switch (event.type()) {
                case JOIN:
                    updateChatRoomUseCase.addMember(event.roomId(), event.userId());
                    break;
                case LEAVE:
                    updateChatRoomUseCase.removeMember(event.roomId(), event.userId());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("consumeMemberModifiedEvent error", e);
        }
    }
}
