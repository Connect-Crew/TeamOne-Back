package com.connectcrew.teamone.chatservice.chatroom.application.port.in;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import com.connectcrew.teamone.chatservice.chatroom.domain.enums.ChatRoomType;

import java.util.Set;

public interface CreateChatRoomUseCase {
    ChatRoom createChatRoom(ChatRoomType type, Set<Long> users);
}
