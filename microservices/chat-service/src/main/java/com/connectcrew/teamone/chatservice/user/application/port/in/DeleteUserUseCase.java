package com.connectcrew.teamone.chatservice.user.application.port.in;

import org.springframework.web.socket.WebSocketSession;

public interface DeleteUserUseCase {
    void deleteUserSession(WebSocketSession session);
}
