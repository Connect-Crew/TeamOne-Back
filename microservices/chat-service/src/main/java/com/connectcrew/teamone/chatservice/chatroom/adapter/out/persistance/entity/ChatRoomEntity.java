package com.connectcrew.teamone.chatservice.chatroom.adapter.out.persistance.entity;

import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import com.connectcrew.teamone.chatservice.chatroom.domain.enums.ChatRoomType;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Builder
@Document(collection = "chatroom")
public class ChatRoomEntity {
    @Id
    private UUID id;

    private ChatRoomType type;

    private Set<Long> members;

    public static ChatRoomEntity toEntity(ChatRoom chatRoom) {
        return ChatRoomEntity.builder()
                .id(chatRoom.id())
                .type(chatRoom.type())
                .members(chatRoom.members())
                .build();
    }

    public ChatRoom toDomain() {
        return new ChatRoom(id, type, members);
    }
}
