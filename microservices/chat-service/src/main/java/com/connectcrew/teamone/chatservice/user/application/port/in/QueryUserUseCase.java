package com.connectcrew.teamone.chatservice.user.application.port.in;

import com.connectcrew.teamone.chatservice.user.domain.vo.UserSession;

import java.util.Optional;

public interface QueryUserUseCase {
    Optional<UserSession> findUserSessionByUserId(Long userId);
}
