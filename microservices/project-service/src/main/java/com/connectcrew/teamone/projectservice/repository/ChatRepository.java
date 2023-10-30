package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.Chat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChatRepository extends ReactiveCrudRepository<Chat, Long> {
}
