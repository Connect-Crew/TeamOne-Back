package com.connectcrew.teamone.chatservice.chatroom.adapter.in.web;

import com.connectcrew.teamone.chatservice.chatroom.adapter.in.web.request.ChatRoomRequest;
import com.connectcrew.teamone.chatservice.chatroom.adapter.in.web.response.ChatRoomResponse;
import com.connectcrew.teamone.chatservice.chatroom.application.port.in.CreateChatRoomUseCase;
import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {
    private final CreateChatRoomUseCase createChatRoomUseCase;

    @PostMapping()
    public ChatRoomResponse createChatRoom(@RequestBody ChatRoomRequest request) {
        ChatRoom chatroom = createChatRoomUseCase.createChatRoom(request.type(), request.members());
        return ChatRoomResponse.from(chatroom);
    }
}
