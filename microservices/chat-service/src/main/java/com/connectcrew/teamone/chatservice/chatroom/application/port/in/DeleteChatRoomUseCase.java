package com.connectcrew.teamone.chatservice.chatroom.application.port.in;

import java.util.UUID;

public interface DeleteChatRoomUseCase {
    void deleteChatRoom(UUID roomId);
}
