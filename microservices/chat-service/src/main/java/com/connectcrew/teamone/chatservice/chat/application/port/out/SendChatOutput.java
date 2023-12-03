package com.connectcrew.teamone.chatservice.chat.application.port.out;

import com.connectcrew.teamone.chatservice.chat.domain.Chat;
import com.connectcrew.teamone.chatservice.user.domain.vo.UserSession;

public interface SendChatOutput {
    void sendChat(Chat chat, UserSession session);
}
