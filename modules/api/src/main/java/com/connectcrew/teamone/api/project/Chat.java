package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.ChatType;

import java.time.LocalDateTime;

public record Chat(
        Long chatId,
        Long projectId,
        Long senderId,
        ChatType type,
        String message,
        LocalDateTime time
) {
}
