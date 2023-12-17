package com.connectcrew.teamone.compositeservice.composite.application.port.in.command;

import com.connectcrew.teamone.compositeservice.composite.domain.ChatRoom;
import com.connectcrew.teamone.compositeservice.global.enums.ChatRoomType;

import java.util.Set;
import java.util.UUID;

public record CreateChatRoomCommand(
        ChatRoomType type,
        Set<Long> members
) {

    public ChatRoom toDomain(UUID id) {
        return new ChatRoom(id, type, members);
    }
}
