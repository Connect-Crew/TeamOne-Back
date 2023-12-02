package com.connectcrew.teamone.chatservice.chat.adapter.in.web.response;

import com.connectcrew.teamone.chatservice.chat.domain.Chat;
import com.connectcrew.teamone.chatservice.chat.domain.enums.MessageType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private MessageType type;
    private Long sender;
    private UUID roomId;
    private String message;
    private LocalDateTime timestamp;

    public static ChatResponse toResponse(Chat chat) {
        return ChatResponse.builder()
                .type(chat.type())
                .sender(chat.sender())
                .roomId(chat.roomId())
                .message(chat.content())
                .timestamp(chat.timestamp())
                .build();
    }
}
