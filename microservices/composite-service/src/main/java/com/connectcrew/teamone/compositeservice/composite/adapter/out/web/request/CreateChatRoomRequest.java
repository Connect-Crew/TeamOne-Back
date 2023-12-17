package com.connectcrew.teamone.compositeservice.composite.adapter.out.web.request;

import com.connectcrew.teamone.compositeservice.global.enums.ChatRoomType;

import java.util.Set;

public record CreateChatRoomRequest(
        ChatRoomType type,
        Set<Long> members
) {
}
