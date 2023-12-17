package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.compositeservice.composite.domain.ChatRoom;
import com.connectcrew.teamone.compositeservice.global.enums.ChatRoomType;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface CreateChatRoomOutput {
    Mono<ChatRoom> createChatRoom(ChatRoomType type, Set<Long> members);
}
