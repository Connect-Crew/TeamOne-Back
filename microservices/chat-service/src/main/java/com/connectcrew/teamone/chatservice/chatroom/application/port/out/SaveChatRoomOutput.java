package com.connectcrew.teamone.chatservice.chatroom.application.port.out;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;

public interface SaveChatRoomOutput {
    ChatRoom saveChatRoom(ChatRoom chatRoom);
}
