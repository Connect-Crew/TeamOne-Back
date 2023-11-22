package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.compositeservice.exception.WebClientExceptionHandler;
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
    public Mono<String> createRoom() {
        return webClient.get()
                .uri(String.format("%s/chat", host))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(exHandler::handleException);
    }
}
