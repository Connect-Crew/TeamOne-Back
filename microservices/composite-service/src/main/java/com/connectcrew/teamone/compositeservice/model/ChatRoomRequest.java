package com.connectcrew.teamone.compositeservice.model;

import com.connectcrew.teamone.compositeservice.model.enums.ChatRoomType;

import java.util.Set;

public record ChatRoomRequest(
        ChatRoomType type,
        Set<Long> members
) {
}
