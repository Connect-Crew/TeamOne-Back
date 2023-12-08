package com.connectcrew.teamone.chatservice.user.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserExceptionMessage {
    USER_NOT_FOUND("유저를 찾을 수 없습니다."),

    ;
    private final String message;

    public String getMessage() {
        return message;
    }
}
