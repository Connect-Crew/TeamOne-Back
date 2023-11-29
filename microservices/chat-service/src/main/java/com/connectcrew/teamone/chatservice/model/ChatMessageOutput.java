package com.connectcrew.teamone.chatservice.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageOutput {
    private MessageType type;
    private Long userId;
    private String nickname;
    private String roomId;
    private String message;
    private String timestamp;

    public ChatMessageOutput(MessageType type, Long userId, String nickname, String roomId, String message) {
        this.type = type;
        this.userId = userId;
        this.nickname = nickname;
        this.roomId = roomId;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
    }

    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert ChatMessageOutput to JSON string", e);
        }
    }

    // JSON 문자열을 객체로 역직렬화하는 메서드
    public static ChatMessageOutput fromString(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, ChatMessageOutput.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON string to ChatMessageOutput", e);
        }
    }

}
