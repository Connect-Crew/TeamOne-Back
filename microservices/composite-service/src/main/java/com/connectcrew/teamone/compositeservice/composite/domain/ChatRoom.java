package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.global.enums.ChatRoomType;

import java.util.Set;
import java.util.UUID;

public record ChatRoom(
        UUID id,
        ChatRoomType type,
        Set<Long> members
) {
}
