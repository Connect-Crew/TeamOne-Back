package com.connectcrew.teamone.api.exception.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserExceptionMessage {
    NOT_FOUND_USER("유저를 찾을수 없습니다."),
    NOT_FOUND_USER_FCM_TOKEN("유저의 FCM 토큰을 찾을수 없습니다."),

    ;
    private final String message;

    @Override
    public String toString() {
        return message;
    }
}
