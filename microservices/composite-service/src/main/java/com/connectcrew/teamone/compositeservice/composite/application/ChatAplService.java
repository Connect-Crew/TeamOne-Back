package com.connectcrew.teamone.compositeservice.composite.application;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.CreateChatRoomUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateChatRoomCommand;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.CreateChatRoomOutput;
import com.connectcrew.teamone.compositeservice.composite.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatAplService implements CreateChatRoomUseCase {

    private final CreateChatRoomOutput createChatRoomOutput;

    @Override
    public Mono<ChatRoom> createChatRoom(CreateChatRoomCommand command) {
//        return createChatRoomOutput.createChatRoom(command.type(), command.members());
        // TODO Chatting Serialize 문제 해결 후 적용
        return Mono.just(new ChatRoom(UUID.randomUUID(), command.type(), command.members()));
    }
}
