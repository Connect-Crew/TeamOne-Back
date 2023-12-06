package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.compositeservice.exception.WebClientExceptionHandler;
import com.connectcrew.teamone.compositeservice.model.ChatRoomRequest;
import com.connectcrew.teamone.compositeservice.model.ChatRoomResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ChatRequestImpl implements ChatRequest {

    public final String host;

    private final WebClient webClient;

    private final WebClientExceptionHandler exHandler;

    public ChatRequestImpl(String host, WebClient webClient) {
        this.host = host;
        this.webClient = webClient;
        this.exHandler = new WebClientExceptionHandler();
    }
    @Override
    public Mono<ChatRoomResponse> createChatRoom(ChatRoomRequest chatRoomRequest) {
        return webClient.post()
                .uri(String.format("%s/chatroom", host))
                .bodyValue(chatRoomRequest)
                .retrieve()
                .bodyToMono(ChatRoomResponse.class)
                .onErrorResume(exHandler::handleException);
    }
}
