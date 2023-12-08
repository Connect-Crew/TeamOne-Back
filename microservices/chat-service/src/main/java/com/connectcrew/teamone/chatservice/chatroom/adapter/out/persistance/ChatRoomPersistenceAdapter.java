package com.connectcrew.teamone.chatservice.chatroom.adapter.out.persistance;

import com.connectcrew.teamone.chatservice.chatroom.adapter.out.persistance.entity.ChatRoomEntity;
import com.connectcrew.teamone.chatservice.chatroom.adapter.out.persistance.repository.ChatRoomRepository;
import com.connectcrew.teamone.chatservice.chatroom.application.port.out.DeleteChatRoomOutput;
import com.connectcrew.teamone.chatservice.chatroom.application.port.out.FindChatRoomOutput;
import com.connectcrew.teamone.chatservice.chatroom.application.port.out.SaveChatRoomOutput;
import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatRoomPersistenceAdapter implements SaveChatRoomOutput, FindChatRoomOutput, DeleteChatRoomOutput {
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom saveChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(ChatRoomEntity.toEntity(chatRoom)).toDomain();
    }

    @Override
    public void deleteChatRoom(UUID roomId) {
        chatRoomRepository.deleteById(roomId);
    }

    @Override
    public Optional<ChatRoom> findById(UUID roomId) {
        return chatRoomRepository.findById(roomId).map(ChatRoomEntity::toDomain);
    }

    @Override
    public Set<ChatRoom> findAllByIds(Set<UUID> roomIds) {
        return chatRoomRepository.findAllById(roomIds)
                .stream()
                .map(ChatRoomEntity::toDomain)
                .collect(Collectors.toSet());
    }
}
