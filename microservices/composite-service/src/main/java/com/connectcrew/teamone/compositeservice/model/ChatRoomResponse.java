package com.connectcrew.teamone.compositeservice.model;

import com.connectcrew.teamone.compositeservice.model.enums.ChatRoomType;

import java.util.Set;
import java.util.UUID;

public record ChatRoomResponse(
        UUID id,
        ChatRoomType type,
        Set<Long> members
) {
}
