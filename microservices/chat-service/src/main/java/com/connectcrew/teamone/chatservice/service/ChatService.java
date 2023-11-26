package com.connectcrew.teamone.chatservice.service;

import com.connectcrew.teamone.chatservice.model.ChatMessageOutput;
import com.connectcrew.teamone.chatservice.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private final Map<Long, User> users;

    public ChatService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.users = new HashMap<>();
    }

    public void broadcastMessage(ChatMessageOutput message) {

    }

    public void addUser(User user) {
        users.put(user.id(), user);
    }

    public boolean existsUser(Long userId) {
        return users.containsKey(userId);
    }

    public boolean isJoinedChatRoom(Long userId, String chatRoomId) {
        User user = users.get(userId);
        return user.chatRooms().contains(chatRoomId);
    }

    public void removeUser(WebSocketSession session) {
        for (User user : users.values()) {
            if (user.session().equals(session)) {
                log.trace("remove websocket session user: {}", user.id());
                users.remove(user.id());
                return;
            }
        }
    }
}