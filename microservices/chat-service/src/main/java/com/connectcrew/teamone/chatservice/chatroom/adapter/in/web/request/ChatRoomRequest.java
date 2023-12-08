package com.connectcrew.teamone.chatservice.chatroom.adapter.in.web.request;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import com.connectcrew.teamone.chatservice.chatroom.domain.enums.ChatRoomType;

import java.util.Set;

public record ChatRoomRequest(
        ChatRoomType type,
        Set<Long> members
) {

}
