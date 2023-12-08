package com.connectcrew.teamone.chatservice.chat.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatExceptionMessage {
    INVALID_TOKEN("토큰이 유효하지 않습니다."),
    ;
    private final String message;
}
