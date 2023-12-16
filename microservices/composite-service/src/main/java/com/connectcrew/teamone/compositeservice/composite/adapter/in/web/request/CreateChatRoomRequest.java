package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import com.connectcrew.teamone.compositeservice.model.enums.ChatRoomType;

import java.util.Set;

public record CreateChatRoomRequest(
        ChatRoomType type,
        Set<Long> members
) {
}
