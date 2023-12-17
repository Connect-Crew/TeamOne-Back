package com.connectcrew.teamone.compositeservice.composite.adapter.out.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.ChatRoom;
import com.connectcrew.teamone.compositeservice.global.enums.ChatRoomType;

import java.util.Set;
import java.util.UUID;

public record ChatRoomResponse(
        UUID id,
        ChatRoomType type,
        Set<Long> members
) {

    public ChatRoom toDomain() {
        return new ChatRoom(id, type, members);
    }
}
