package com.connectcrew.teamone.chatservice.user.adapter.in.event;

import com.connectcrew.teamone.chatservice.chatroom.adapter.in.event.MemberModifiedEvent;
import com.connectcrew.teamone.chatservice.user.application.port.in.UpdateUserUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomMemberModifiedEventListener implements MessageListener {
    private final UpdateUserUseCase updateChatRoomUseCase;
    private final ObjectMapper objectMapper;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MemberModifiedEvent event = objectMapper.readValue(message.toString(), MemberModifiedEvent.class);

            updateChatRoomUseCase.updateUserMemberOnSession(event.roomId(), event.type(), event.userId());
        } catch (Exception e) {
            log.error("onMessage error", e);
        }

    }
}
