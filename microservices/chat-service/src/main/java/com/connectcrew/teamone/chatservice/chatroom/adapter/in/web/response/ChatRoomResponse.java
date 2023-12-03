package com.connectcrew.teamone.chatservice.chatroom.adapter.in.web.response;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import com.connectcrew.teamone.chatservice.chatroom.domain.enums.ChatRoomType;

import java.util.Set;
import java.util.UUID;

public record ChatRoomResponse(
        UUID id,
        ChatRoomType type,
        Set<Long> members
) {
    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return new ChatRoomResponse(chatRoom.id(), chatRoom.type(), chatRoom.members());
    }
}
