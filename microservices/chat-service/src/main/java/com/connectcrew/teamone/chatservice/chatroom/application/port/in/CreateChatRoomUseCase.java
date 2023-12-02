package com.connectcrew.teamone.chatservice.chatroom.application.port.in;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;

import java.util.Set;

public interface CreateChatRoomUseCase {
    ChatRoom createChatRoom(Set<Long> users);
}
