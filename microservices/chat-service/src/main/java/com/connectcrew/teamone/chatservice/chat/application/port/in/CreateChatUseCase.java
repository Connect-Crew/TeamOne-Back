package com.connectcrew.teamone.chatservice.chat.application.port.in;

import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

public interface CreateChatUseCase {
    void createChat(UUID roomId, Long senderId, String message, WebSocketSession session);
}
