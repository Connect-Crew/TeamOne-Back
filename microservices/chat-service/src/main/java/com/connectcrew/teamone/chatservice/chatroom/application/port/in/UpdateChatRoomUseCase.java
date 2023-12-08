package com.connectcrew.teamone.chatservice.chatroom.application.port.in;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;

import java.util.UUID;

public interface UpdateChatRoomUseCase {
    ChatRoom addMember(UUID roomId, Long userId);

    ChatRoom removeMember(UUID roomId, Long userId);
}
