package com.connectcrew.teamone.chatservice.chat.adapter.in.web;

import com.connectcrew.teamone.chatservice.chat.adapter.in.web.response.ChatResponse;
import com.connectcrew.teamone.chatservice.chat.application.port.in.QueryChatUseCase;
import com.connectcrew.teamone.chatservice.chat.application.port.in.query.SearchChatPageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController("/chat")
public class ChatController {
    private final QueryChatUseCase queryChatUseCase;

    @GetMapping()
    public List<ChatResponse> getChatByRoomId(String roomId, int page, int size) {
        return queryChatUseCase.searchChatPage(new SearchChatPageQuery(roomId, page, size))
                .stream()
                .map(ChatResponse::toResponse)
                .toList();
    }
}
