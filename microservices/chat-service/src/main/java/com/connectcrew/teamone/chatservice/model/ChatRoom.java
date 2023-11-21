package com.connectcrew.teamone.chatservice.model;

import com.connectcrew.teamone.chatservice.service.ChatService;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

public record ChatRoom(
        String roomId,
        Set<WebSocketSession> sessions
) {

    public ChatRoom(String roomId) {
        this(roomId, new HashSet<>());
    }

    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        switch (chatMessage.type()) {
            case ENTER -> {
                sessions.add(session);
                chatMessage = new ChatMessage(MessageType.ENTER, chatMessage.roomId(), chatMessage.sender(), chatMessage.sender() + "님이 입장하셨습니다.");
            }
            case TALK -> {

            }
        }

        sendMessage(chatMessage, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }
}