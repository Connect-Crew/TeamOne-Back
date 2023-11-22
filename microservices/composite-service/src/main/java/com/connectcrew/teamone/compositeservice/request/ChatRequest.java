package com.connectcrew.teamone.compositeservice.request;

import reactor.core.publisher.Mono;

public interface ChatRequest {
    Mono<String> createRoom();
}
