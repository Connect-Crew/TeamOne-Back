package com.connectcrew.teamone.chatservice.user.application.port.in;

import com.connectcrew.teamone.chatservice.user.domain.User;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface UpdateUserUseCase {
    void updateUserSessionIfNotExists(Long userId, WebSocketSession session);

    void updateUser(User user);

    void updateAll(List<User> users);
}
