package com.connectcrew.teamone.chatservice.repository;

import com.connectcrew.teamone.chatservice.model.ChatEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<ChatEntity, String> {
    List<ChatEntity> findAllByRoomId(String roomId);
}
