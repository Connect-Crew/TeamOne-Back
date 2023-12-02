package com.connectcrew.teamone.chatservice.chat.application.port.out;

import com.connectcrew.teamone.chatservice.chat.domain.Chat;

public interface SaveChatOutput {
    Chat save(Chat chat);
}
