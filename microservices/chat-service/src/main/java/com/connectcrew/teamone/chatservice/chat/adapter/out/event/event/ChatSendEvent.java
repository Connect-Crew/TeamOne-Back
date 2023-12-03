package com.connectcrew.teamone.chatservice.chat.adapter.out.event.event;

import com.connectcrew.teamone.chatservice.chat.domain.enums.MessageType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ChatSendEvent(
        MessageType type,
        Long sender,
        UUID roomId,
        Set<Long> receivers,
        String content,
        LocalDateTime timestamp
) {
}
