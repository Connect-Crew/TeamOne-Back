package com.connectcrew.teamone.chatservice.model;

public record ChatMessageOutput(
        MessageType type,
        Long userId,
        String nickname,
        String roomId,
        String message
) {

}
