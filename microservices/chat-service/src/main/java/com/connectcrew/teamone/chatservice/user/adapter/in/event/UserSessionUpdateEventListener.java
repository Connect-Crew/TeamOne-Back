package com.connectcrew.teamone.chatservice.user.adapter.in.event;

import com.connectcrew.teamone.chatservice.chatroom.adapter.out.event.event.ChatRoomCreatedEvent;
import com.connectcrew.teamone.chatservice.chatroom.application.port.in.UpdateChatRoomUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSessionUpdateEventListener implements MessageListener {
    private UpdateChatRoomUseCase updateChatRoomUseCase;
    private final ObjectMapper objectMapper;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatRoomCreatedEvent event = objectMapper.readValue(message.toString(), ChatRoomCreatedEvent.class);

            updateChatRoomUseCase.updateUserSessionJoinInfo(event.roomId(), event.members());
        } catch (Exception e) {
            log.error("onMessage error", e);
        }

    }
}
