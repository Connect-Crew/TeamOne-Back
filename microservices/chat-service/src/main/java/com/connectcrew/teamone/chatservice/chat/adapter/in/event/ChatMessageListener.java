package com.connectcrew.teamone.chatservice.chat.adapter.in.event;

import com.connectcrew.teamone.chatservice.chat.application.port.in.SendChatUseCase;
import com.connectcrew.teamone.chatservice.chat.domain.Chat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final SendChatUseCase sendChatUseCase;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {

            Chat chat = objectMapper.readValue(message.getBody(), Chat.class);

            sendChatUseCase.sendChat(chat);
        } catch (Exception e) {
            log.error("onMessage error", e);
        }
    }
}