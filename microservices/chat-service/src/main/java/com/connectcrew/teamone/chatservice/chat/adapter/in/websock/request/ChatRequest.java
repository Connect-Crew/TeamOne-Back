package com.connectcrew.teamone.chatservice.chat.adapter.in.websock.request;

import java.util.UUID;

public record ChatRequest(
        String token,
        UUID roomId,
        String message
) {
}
