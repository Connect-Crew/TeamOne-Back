package com.connectcrew.teamone.chatservice.chatroom.domain.factory;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import com.connectcrew.teamone.chatservice.chatroom.domain.enums.ChatRoomType;

import java.util.Set;
import java.util.UUID;

public class ChatRoomFactory {

    public static ChatRoom newInstance(ChatRoomType type, Set<Long> userIds) {
        return new ChatRoom(UUID.randomUUID(), type, userIds);
    }
}
