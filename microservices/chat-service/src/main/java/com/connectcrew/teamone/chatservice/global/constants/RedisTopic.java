package com.connectcrew.teamone.chatservice.global.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisTopic {
    CHAT_ROOM_CREATED("chat-room-created"),
    MEMBER_MODIFIED("member-modified"),
    CHAT("chat")


    ;
    private final String topic;
}
