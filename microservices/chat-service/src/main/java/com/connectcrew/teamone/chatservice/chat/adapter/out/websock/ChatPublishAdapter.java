package com.connectcrew.teamone.chatservice.chat.adapter.out.websock;

import com.connectcrew.teamone.chatservice.chat.adapter.out.websock.publisher.ChatPublisher;
import com.connectcrew.teamone.chatservice.chat.application.port.out.PublishChatOutput;
import com.connectcrew.teamone.chatservice.chat.domain.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatPublishAdapter implements PublishChatOutput {
    private final ChatPublisher chatPublisher;

    @Override
    public void publish(Chat chat) {
        chatPublisher.publish(chat);
    }
}
