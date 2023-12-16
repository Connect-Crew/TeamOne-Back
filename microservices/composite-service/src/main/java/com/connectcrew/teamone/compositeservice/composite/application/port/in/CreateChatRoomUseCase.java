package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateChatRoomCommand;
import com.connectcrew.teamone.compositeservice.composite.domain.ChatRoom;
import reactor.core.publisher.Mono;

public interface CreateChatRoomUseCase {
    Mono<ChatRoom> createChatRoom(CreateChatRoomCommand command);
}
