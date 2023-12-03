package com.connectcrew.teamone.chatservice.chatroom.adapter.out.event;

import com.connectcrew.teamone.chatservice.chatroom.adapter.out.event.event.ChatRoomCreatedEvent;
import com.connectcrew.teamone.chatservice.chatroom.adapter.out.event.publisher.ChatRoomCreatedEventPublisher;
import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import com.connectcrew.teamone.chatservice.global.constants.RedisTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisChatRoomCreatedPublisher implements ChatRoomCreatedEventPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public void publish(ChatRoom chatRoom) {
        try {
            String message = objectMapper.writeValueAsString(ChatRoomCreatedEvent.of(chatRoom));
            redisTemplate.convertAndSend(RedisTopic.CHAT.getTopic(), message);
        } catch (Exception e) {
            log.error("publish error", e);
        }
    }
}
