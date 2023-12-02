package com.connectcrew.teamone.chatservice.chatroom.domain.factory;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;

import java.util.Set;
import java.util.UUID;

public class ChatRoomFactory {

    public static ChatRoom newInstance(Set<Long> userIds) {
        return new ChatRoom(UUID.randomUUID(), userIds);
    }
}
