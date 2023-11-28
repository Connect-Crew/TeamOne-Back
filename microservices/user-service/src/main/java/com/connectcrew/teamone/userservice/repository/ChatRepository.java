package com.connectcrew.teamone.userservice.repository;

import com.connectcrew.teamone.userservice.entity.ChatCustomEntity;
import reactor.core.publisher.Flux;

public interface ChatRepository {
    Flux<ChatCustomEntity> findAllChatIdAndMembersByUserId(Long userId);

}
