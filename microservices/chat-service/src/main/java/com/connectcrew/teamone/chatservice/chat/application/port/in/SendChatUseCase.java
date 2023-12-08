package com.connectcrew.teamone.chatservice.chat.application.port.in;

import com.connectcrew.teamone.chatservice.chat.domain.Chat;

public interface SendChatUseCase {
    void sendChat(Chat chat);
}
