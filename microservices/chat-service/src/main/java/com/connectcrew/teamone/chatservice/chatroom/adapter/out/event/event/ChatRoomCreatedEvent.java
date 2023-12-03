package com.connectcrew.teamone.chatservice.chatroom.adapter.out.event.event;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;

import java.util.Set;
import java.util.UUID;

public record ChatRoomCreatedEvent(
        UUID roomId,
        Set<Long> members
) {
    public static ChatRoomCreatedEvent of(ChatRoom chatRoom) {
        return new ChatRoomCreatedEvent(chatRoom.id(), chatRoom.members());
    }
}
