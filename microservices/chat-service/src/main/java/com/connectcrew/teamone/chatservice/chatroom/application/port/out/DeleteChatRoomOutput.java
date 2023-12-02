package com.connectcrew.teamone.chatservice.chatroom.application.port.out;

import java.util.UUID;

public interface DeleteChatRoomOutput {
    void deleteChatRoom(UUID roomId);
}
