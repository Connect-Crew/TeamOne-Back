package com.connectcrew.teamone.chatservice.chatroom.adapter.out.event.event;

import com.connectcrew.teamone.chatservice.chatroom.domain.enums.MemberModifiedType;

import java.util.UUID;

public record ChatMemberModifiedEvent(
        UUID roomId,
        MemberModifiedType type,
        Long userId
) {

}
