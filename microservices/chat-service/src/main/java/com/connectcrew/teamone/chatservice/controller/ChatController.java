package com.connectcrew.teamone.chatservice.controller;

import com.connectcrew.teamone.chatservice.model.ChatEntity;
import com.connectcrew.teamone.chatservice.model.ChatMessageOutput;
import com.connectcrew.teamone.chatservice.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController("/chat")
public class ChatController {
    private final ChatRepository chatRepository;

    @GetMapping()
    public List<ChatMessageOutput> getChatByRoomId(String roomId, int page, int size) {
        return chatRepository.findByRoomIdOrderByTimestampDesc(roomId, PageRequest.of(page, size))
                .stream()
                .map(ChatEntity::toOutput)
                .toList();
    }
}
