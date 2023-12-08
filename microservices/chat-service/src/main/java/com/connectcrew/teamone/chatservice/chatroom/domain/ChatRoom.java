package com.connectcrew.teamone.chatservice.chatroom.domain;

import com.connectcrew.teamone.chatservice.chatroom.domain.enums.ChatRoomType;

import java.util.Set;
import java.util.UUID;

public record ChatRoom(
        UUID id,
        ChatRoomType type,
        Set<Long> members
) {
    public void addMember(Long userId) {
        members.add(userId);
    }

    public void removeMember(Long userId) {
        members.remove(userId);
    }
}
