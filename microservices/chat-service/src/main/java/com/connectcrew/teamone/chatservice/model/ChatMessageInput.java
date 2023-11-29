package com.connectcrew.teamone.chatservice.model;

public record ChatMessageInput(
        String token,
        String roomId,
        String message
) {
}
