package com.connectcrew.teamone.chatservice.chat.adapter.out.websock;

import com.connectcrew.teamone.chatservice.chat.adapter.out.websock.publisher.ChatPublisher;
import com.connectcrew.teamone.chatservice.chat.domain.Chat;
import com.connectcrew.teamone.chatservice.global.constants.RedisTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMessagePublisher implements ChatPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public void publish(Chat chat) {
        try {
            String message = objectMapper.writeValueAsString(chat);
            log.trace("publish message: {}", message);
            redisTemplate.convertAndSend(RedisTopic.CHAT.getTopic(), message);
        } catch (Exception e) {
            log.error("publish error", e);
        }
    }
}
