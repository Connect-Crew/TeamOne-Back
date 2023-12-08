package com.connectcrew.teamone.chatservice.chatroom.adapter.out.event;

import com.connectcrew.teamone.chatservice.chatroom.adapter.in.event.MemberModifiedEvent;
import com.connectcrew.teamone.chatservice.chatroom.adapter.out.event.event.ChatRoomCreatedEvent;
import com.connectcrew.teamone.chatservice.chatroom.adapter.out.event.publisher.ChatRoomCreatedEventPublisher;
import com.connectcrew.teamone.chatservice.chatroom.application.port.out.ModifiedMemberChatRoomEventOutput;
import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import com.connectcrew.teamone.chatservice.chatroom.domain.enums.MemberModifiedType;
import com.connectcrew.teamone.chatservice.global.constants.RedisTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisChatRoomPublisher implements ChatRoomCreatedEventPublisher, ModifiedMemberChatRoomEventOutput {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public void publishChatRoomCreated(ChatRoom chatRoom) {
        try {
            String message = objectMapper.writeValueAsString(ChatRoomCreatedEvent.of(chatRoom));
            redisTemplate.convertAndSend(RedisTopic.CHAT.getTopic(), message);
        } catch (Exception e) {
            log.error("publish error", e);
        }
    }

    @Override
    public void publishModifiedMember(UUID roomId, MemberModifiedType type, Long userId) {
        try {
            String message = objectMapper.writeValueAsString(new MemberModifiedEvent(roomId, type, userId));
            redisTemplate.convertAndSend(RedisTopic.MEMBER_MODIFIED.getTopic(), message);
        } catch (Exception e) {
            log.error("publish error", e);
        }
    }
}
