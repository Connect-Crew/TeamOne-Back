package com.connectcrew.teamone.chatservice.service;

import com.connectcrew.teamone.chatservice.model.ChatMessage;
import com.connectcrew.teamone.chatservice.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private final Map<String, User> chatRooms;

    public ChatService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.chatRooms = new HashMap<>();
    }

    public void broadcastMessage(ChatMessage message) {

    }
}