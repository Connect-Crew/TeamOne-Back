package com.connectcrew.teamone.compositeservice.composite.adapter.out.web;

import com.connectcrew.teamone.compositeservice.composite.application.port.out.CreateChatRoomOutput;
import com.connectcrew.teamone.compositeservice.composite.domain.ChatRoom;
import com.connectcrew.teamone.compositeservice.global.exception.WebClientExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ChatWebAdapter implements CreateChatRoomOutput {

    @Value("${app.chat}")
    public String host;

    private final WebClient webClient;

    private final WebClientExceptionHandler exHandler;

    @Override
    public Mono<ChatRoom> createChatRoom(ChatRoom chatRoom) {
        return webClient.post()
                .uri(String.format("%s/chatroom", host))
                .bodyValue(chatRoom)
                .retrieve()
                .bodyToMono(ChatRoom.class)
                .onErrorResume(exHandler::handleException);
    }
}
