package com.connectcrew.teamone.chatservice.user.domain.vo;

import com.connectcrew.teamone.chatservice.user.domain.User;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

public record UserSession(
        User user,
        WebSocketSession session
) {
    public boolean isJoined(UUID roomId) {
        return user.chatRooms().contains(roomId);
    }
}
