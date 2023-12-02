package com.connectcrew.teamone.chatservice.chat.adapter.out.websock.publisher;

import com.connectcrew.teamone.chatservice.chat.domain.Chat;

public interface ChatPublisher {
    void publish(Chat chat);
}
