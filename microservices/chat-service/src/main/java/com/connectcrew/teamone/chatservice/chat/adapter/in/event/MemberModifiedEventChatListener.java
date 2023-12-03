package com.connectcrew.teamone.chatservice.chat.adapter.in.event;

import com.connectcrew.teamone.chatservice.chat.application.port.in.CreateChatUseCase;
import com.connectcrew.teamone.chatservice.chatroom.adapter.in.event.MemberModifiedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberModifiedEventChatListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final CreateChatUseCase createChatUseCase;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MemberModifiedEvent event = objectMapper.readValue(message.toString(), MemberModifiedEvent.class);

            createChatUseCase.createNotifyMemberModified(event.roomId(), event.type(), event.userId());
        } catch (Exception e) {
            log.error("onMessage error", e);
        }
    }
}
