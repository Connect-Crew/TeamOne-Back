package com.connectcrew.teamone.chatservice.user.adapter.out.persistence.entity;

import com.connectcrew.teamone.chatservice.user.domain.User;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Builder
@Document(collection = "user")
public class UserEntity {
    Long id;

    Set<UUID> chatRooms;

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.id())
                .chatRooms(user.chatRooms())
                .build();
    }

    public User toDomain() {
        return new User(id, chatRooms);
    }
}
