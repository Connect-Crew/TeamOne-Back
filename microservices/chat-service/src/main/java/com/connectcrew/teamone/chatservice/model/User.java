package com.connectcrew.teamone.chatservice.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;

@Slf4j
public record User(
        Long id,
        String username,
        String currentChatRoomId,
        Set<ChatRoom> chatRooms,
        WebSocketSession session
) {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public boolean isJoinedChatRoom(String chatRoomId) {
        return chatRooms.stream().anyMatch(c -> c.id().equals(chatRoomId));
    }

    public void sendMessage(ChatMessageOutput message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
