package com.connectcrew.teamone.chatservice.chat.domain;

import com.connectcrew.teamone.chatservice.chat.domain.enums.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record Chat(
        MessageType type,
        Long sender,
        UUID roomId,
        String content,
        LocalDateTime timestamp
) {
}
