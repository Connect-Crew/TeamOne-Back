package com.connectcrew.teamone.chatservice.chat.adapter.out.websock;

import com.connectcrew.teamone.chatservice.chat.adapter.in.web.response.ChatResponse;
import com.connectcrew.teamone.chatservice.chat.application.port.out.SendChatOutput;
import com.connectcrew.teamone.chatservice.chat.domain.Chat;
import com.connectcrew.teamone.chatservice.user.domain.vo.UserSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatSendAdapter implements SendChatOutput {
    private final ObjectMapper objectMapper;

    @Override
    public void sendChat(Chat chat, UserSession session) {
        try {
            session.session().sendMessage(new TextMessage(objectMapper.writeValueAsString(ChatResponse.toResponse(chat))));
        } catch (IOException e) {
            log.error("sendChat error", e);
        }
    }
}
