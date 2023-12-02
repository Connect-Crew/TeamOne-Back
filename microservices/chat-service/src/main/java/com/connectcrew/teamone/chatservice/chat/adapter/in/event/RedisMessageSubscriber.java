package com.connectcrew.teamone.chatservice.chat.adapter.in.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

//    private final ChatService chatService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
//        ChatResponse chatMessage = ChatResponse.fromString(message.toString());
//
//        chatService.broadcastMessage(chatMessage);
    }
}