package com.connectcrew.teamone.compositeservice.composite.adapter.out.web;

import com.connectcrew.teamone.compositeservice.composite.adapter.out.web.request.CreateChatRoomRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.out.web.response.ChatRoomResponse;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.CreateChatRoomOutput;
import com.connectcrew.teamone.compositeservice.composite.domain.ChatRoom;
import com.connectcrew.teamone.compositeservice.global.error.adapter.out.WebClientExceptionHandler;
import com.connectcrew.teamone.compositeservice.global.enums.ChatRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatWebAdapter implements CreateChatRoomOutput {

    @Value("${app.chat}")
    public String host;

    private final WebClient webClient;

    private final WebClientExceptionHandler exHandler;

    @Override
    public Mono<ChatRoom> createChatRoom(ChatRoomType type, Set<Long> members) {
        return webClient.post()
                .uri(String.format("%s/chatroom", host))
                .bodyValue(new CreateChatRoomRequest(type, members))
                .retrieve()
                .bodyToMono(ChatRoomResponse.class)
                .onErrorResume(exHandler::handleException)
                .map(ChatRoomResponse::toDomain);
    }
}
