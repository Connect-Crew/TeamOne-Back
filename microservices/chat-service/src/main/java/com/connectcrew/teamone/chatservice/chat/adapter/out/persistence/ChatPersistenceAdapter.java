package com.connectcrew.teamone.chatservice.chat.adapter.out.persistence;

import com.connectcrew.teamone.chatservice.chat.adapter.out.persistence.entity.ChatEntity;
import com.connectcrew.teamone.chatservice.chat.adapter.out.persistence.repository.ChatRepository;
import com.connectcrew.teamone.chatservice.chat.application.port.out.FindChatOutput;
import com.connectcrew.teamone.chatservice.chat.application.port.out.SaveChatOutput;
import com.connectcrew.teamone.chatservice.chat.domain.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatPersistenceAdapter implements FindChatOutput, SaveChatOutput {

    private final ChatRepository chatRepository;

    @Override
    public List<Chat> findAllByRoomIdAndPageNumberAndPageSize(String roomId, int pageNumber, int pageSize) {
        return chatRepository.findByRoomIdOrderByTimestampDesc(roomId, PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(ChatEntity::toDomain)
                .toList();
    }

    @Override
    public Chat save(Chat chat) {
        return chatRepository.save(ChatEntity.toEntity(chat)).toDomain();
    }
}