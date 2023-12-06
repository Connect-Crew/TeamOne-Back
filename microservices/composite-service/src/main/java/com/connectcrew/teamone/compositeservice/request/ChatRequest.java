package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.compositeservice.model.ChatRoomRequest;
import com.connectcrew.teamone.compositeservice.model.ChatRoomResponse;
import reactor.core.publisher.Mono;

public interface ChatRequest {
    Mono<ChatRoomResponse> createChatRoom(ChatRoomRequest chatRoomRequest);
}
