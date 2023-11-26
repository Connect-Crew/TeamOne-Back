package com.connectcrew.teamone.chatservice.model;


import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Document(collection = "chat")
public class ChatEntity {
    @Id
    private String id;

    private String type;

    private Long userId;

    private String nickname;

    private String roomId;

    private String message;

    private LocalDateTime timestamp;

    public static ChatEntity toEntity(ChatMessageOutput message) {
        return ChatEntity.builder()
                .type(message.type().name())
                .userId(message.userId())
                .nickname(message.nickname())
                .roomId(message.roomId())
                .message(message.message())
                .timestamp(message.timestamp())
                .build();
    }
}
