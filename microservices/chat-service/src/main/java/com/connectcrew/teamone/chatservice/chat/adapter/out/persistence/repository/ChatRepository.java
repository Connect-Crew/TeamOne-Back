package com.connectcrew.teamone.chatservice.chat.adapter.out.persistence.repository;

import com.connectcrew.teamone.chatservice.chat.adapter.out.persistence.entity.ChatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<ChatEntity, String> {
    Page<ChatEntity> findByRoomIdOrderByTimestampDesc(String roomId, Pageable pageable);
}