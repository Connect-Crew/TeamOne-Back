package com.connectcrew.teamone.chatservice.chat.application.port.in;

import com.connectcrew.teamone.chatservice.chatroom.domain.enums.MemberModifiedType;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

public interface CreateChatUseCase {
    void createChat(UUID roomId, Long senderId, String message, WebSocketSession session);

    void createNotifyMemberModified(UUID roomId, MemberModifiedType type, Long userId);
}
