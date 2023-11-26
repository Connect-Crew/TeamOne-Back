package com.connectcrew.teamone.chatservice.service;

import com.connectcrew.teamone.chatservice.model.ChatMessageOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private final ChatService chatService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatMessageOutput chatMessage = objectMapper.readValue(message.getBody(), ChatMessageOutput.class);

            chatService.broadcastMessage(chatMessage);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}