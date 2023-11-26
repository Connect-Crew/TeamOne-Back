package com.connectcrew.teamone.chatservice.model;

import java.time.LocalDateTime;

public record ChatMessageOutput(
        MessageType type,
        Long userId,
        String nickname,
        String roomId,
        String message,
        LocalDateTime timestamp
) {

    public ChatMessageOutput(MessageType type, Long userId, String nickname, String roomId, String message) {
        this(
                type,
                userId,
                nickname,
                roomId,
                message,
                LocalDateTime.now()
        );
    }

}
