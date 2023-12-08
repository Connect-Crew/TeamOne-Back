package com.connectcrew.teamone.chatservice.chatroom.application.port.out;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;

public interface CreateChatRoomEventOutput {
    void publish(ChatRoom chatRoom);
}
