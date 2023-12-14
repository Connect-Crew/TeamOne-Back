package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.compositeservice.composite.domain.ChatRoom;
import reactor.core.publisher.Mono;

public interface CreateChatRoomOutput {
    Mono<ChatRoom> createChatRoom(ChatRoom chatRoom);
}
