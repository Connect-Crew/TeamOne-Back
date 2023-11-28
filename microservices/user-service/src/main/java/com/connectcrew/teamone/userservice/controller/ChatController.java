package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.chat.ChatInfo;
import com.connectcrew.teamone.userservice.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRepository chatRepository;

    @GetMapping()
    public Mono<Set<ChatInfo>> getChatInfos(Long userId) {
        return chatRepository.findAllChatIdAndMembersByUserId(userId)
                .map(chatCustomEntity -> new ChatInfo(chatCustomEntity.chatRoomId(), chatCustomEntity.members()))
                .collect(Collectors.toSet());
    }
}
