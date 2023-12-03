package com.connectcrew.teamone.chatservice.chatroom.adapter.out.event.publisher;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;

public interface ChatRoomCreatedEventPublisher {
    void publishChatRoomCreated(ChatRoom chatRoom);
}
