package com.connectcrew.teamone.chatservice.chat.application.port.out;

import com.connectcrew.teamone.chatservice.chat.domain.Chat;

public interface PublishChatOutput {
    void publish(Chat chat);
}
