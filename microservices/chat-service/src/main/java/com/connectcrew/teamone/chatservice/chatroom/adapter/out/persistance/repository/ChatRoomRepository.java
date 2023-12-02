package com.connectcrew.teamone.chatservice.chatroom.adapter.out.persistance.repository;

import com.connectcrew.teamone.chatservice.chatroom.adapter.out.persistance.entity.ChatRoomEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ChatRoomRepository extends MongoRepository<ChatRoomEntity, UUID> {

}
