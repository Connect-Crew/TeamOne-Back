package com.connectcrew.teamone.chatservice.chat.adapter.out.persistence.entity;


import com.connectcrew.teamone.chatservice.chat.domain.Chat;
import com.connectcrew.teamone.chatservice.chat.domain.enums.MessageType;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Document(collection = "chat")
public class ChatEntity {
    @Id
    private String id;

    private MessageType type;

    private Long userId;

    private UUID roomId;

    private String message;

    private LocalDateTime timestamp;

    public static ChatEntity toEntity(Chat message) {
        return ChatEntity.builder()
                .type(message.type())
                .userId(message.sender())
                .roomId(message.roomId())
                .message(message.content())
                .timestamp(message.timestamp())
                .build();
    }

    public Chat toDomain() {
        return Chat.builder()
                .type(type)
                .sender(userId)
                .roomId(roomId)
                .content(message)
                .timestamp(timestamp)
                .build();
    }
}
