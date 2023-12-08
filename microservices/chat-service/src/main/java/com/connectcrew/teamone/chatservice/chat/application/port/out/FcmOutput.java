package com.connectcrew.teamone.chatservice.chat.application.port.out;

import com.connectcrew.teamone.chatservice.chat.domain.Chat;

import java.util.Set;

public interface FcmOutput {
    void publish(Chat chat, Set<Long> users);
}
