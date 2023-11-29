package com.connectcrew.teamone.chatservice.model;


import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private String timestamp;

    public static ChatEntity toEntity(ChatMessageOutput message) {
        return ChatEntity.builder()
                .type(message.getType().name())
                .userId(message.getUserId())
                .nickname(message.getNickname())
                .roomId(message.getRoomId())
                .message(message.getMessage())
                .timestamp(message.getTimestamp())
                .build();
    }
}
