package com.connectcrew.teamone.chatservice.model;

import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

public record User(
        Long id,
        String username,
        String currentChatRoomId,
        Set<ChatRoom> chatRooms,
        WebSocketSession session
) {

}
