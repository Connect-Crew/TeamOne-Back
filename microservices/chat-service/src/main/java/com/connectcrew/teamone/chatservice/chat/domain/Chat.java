package com.connectcrew.teamone.chatservice.chat.domain;

import com.connectcrew.teamone.chatservice.chat.domain.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonCreator
    public Chat(
            @JsonProperty("type") MessageType type,
            @JsonProperty("sender") Long sender,
            @JsonProperty("roomId") UUID roomId,
            @JsonProperty("content") String content,
            @JsonProperty("timestamp") LocalDateTime timestamp
    ) {
        this.type = type;
        this.sender = sender;
        this.roomId = roomId;
        this.content = content;
        this.timestamp = timestamp;
    }
}
