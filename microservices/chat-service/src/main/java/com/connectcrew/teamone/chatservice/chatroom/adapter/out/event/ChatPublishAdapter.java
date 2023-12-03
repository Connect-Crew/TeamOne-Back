package com.connectcrew.teamone.chatservice.chatroom.adapter.out.event;

import com.connectcrew.teamone.chatservice.chatroom.adapter.out.event.publisher.ChatRoomCreatedEventPublisher;
import com.connectcrew.teamone.chatservice.chatroom.application.port.out.CreateChatRoomEventOutput;
import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatPublishAdapter implements CreateChatRoomEventOutput {

    private final ChatRoomCreatedEventPublisher chatRoomCreatedEventPublisher;
    @Override
    public void publish(ChatRoom chatRoom) {
        chatRoomCreatedEventPublisher.publishChatRoomCreated(chatRoom);
    }
}
