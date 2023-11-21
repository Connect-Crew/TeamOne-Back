package com.connectcrew.teamone.chatservice.model;

public record ChatMessage(
        MessageType type,
        String roomId,
        Long sender,
        String message
) {
}
