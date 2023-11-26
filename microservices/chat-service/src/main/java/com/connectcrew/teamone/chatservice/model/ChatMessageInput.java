package com.connectcrew.teamone.chatservice.model;

public record ChatMessageInput(
        MessageType type,
        String token,
        String roomId,
        String message
) {
}
