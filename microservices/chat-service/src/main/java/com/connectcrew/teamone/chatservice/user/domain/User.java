package com.connectcrew.teamone.chatservice.user.domain;

import java.util.Set;
import java.util.UUID;

public record User(
        Long id,
        Set<UUID> chatRooms
) {
    public void addChatRoom(UUID chatRoomId) {
        chatRooms.add(chatRoomId);
    }

    public void removeChatRoom(UUID chatRoomId) {
        chatRooms.remove(chatRoomId);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
