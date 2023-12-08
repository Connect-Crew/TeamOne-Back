package com.connectcrew.teamone.chatservice.chatroom.application.port.out;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface FindChatRoomOutput {
    Optional<ChatRoom> findById(UUID roomId);

    Set<ChatRoom> findAllByIds(Set<UUID> roomIds);
}
