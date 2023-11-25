package com.connectcrew.teamone.chatservice.model;

public record ChatMessage(
        MessageType type,
        String token,
        String roomId,
        String message
) {
}
