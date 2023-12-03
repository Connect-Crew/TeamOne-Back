package com.connectcrew.teamone.chatservice.chatroom.adapter.in.event;

import com.connectcrew.teamone.chatservice.chatroom.domain.enums.MemberModifiedType;

import java.util.UUID;

public record MemberModifiedEvent(
        UUID roomId,
        MemberModifiedType type,
        Long userId

) {
}
