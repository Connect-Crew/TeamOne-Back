package com.connectcrew.teamone.chatservice.service;

import com.connectcrew.teamone.chatservice.model.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSockChatHandler extends TextWebSocketHandler {
    private final RedisMessagePublisher redisMessagePublisher;
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.trace("chat payload: {}", payload);

        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

        // TODO 새로운 session인 경우 session 저장

        // TODO 영속성 저장

        // TODO push notification

        redisMessagePublisher.publish(chatMessage);
    }
}
