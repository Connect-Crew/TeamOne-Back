package com.connectcrew.teamone.chatservice.user.application.port.in;

import com.connectcrew.teamone.chatservice.user.domain.vo.UserSession;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueryUserUseCase {
    Optional<UserSession> findUserSessionByUserId(Long userId);

    List<UserSession> findAllUserSessionByRoomId(UUID roomId);
}
