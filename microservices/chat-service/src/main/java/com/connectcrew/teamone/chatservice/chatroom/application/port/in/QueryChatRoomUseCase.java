package com.connectcrew.teamone.chatservice.chatroom.application.port.in;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;

import java.util.Set;
import java.util.UUID;

public interface QueryChatRoomUseCase {
    Set<ChatRoom> findAllByUserId(Long userId);
    ChatRoom findByRoomId(UUID roomId);
}
