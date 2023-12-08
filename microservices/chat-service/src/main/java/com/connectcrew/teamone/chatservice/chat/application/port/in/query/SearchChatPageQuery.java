package com.connectcrew.teamone.chatservice.chat.application.port.in.query;

public record SearchChatPageQuery(
        String roomId,
        int page,
        int size
) {
}
