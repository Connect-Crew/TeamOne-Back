package com.connectcrew.teamone.chatservice.chatroom.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatRoomExceptionMessage{
    CHAT_ROOM_NOT_FOUND("채팅방을 찾을 수 없습니다."),
    NOT_REGISTERED_CHAT_ROOM("참여한 채팅방이 아닙니다.")
    ;
    private final String message;
}
