package com.connectcrew.teamone.chatservice.chatroom.application.port.out;

import com.connectcrew.teamone.chatservice.chatroom.domain.enums.MemberModifiedType;

import java.util.UUID;

public interface ModifiedMemberChatRoomEventOutput {
    void publishModifiedMember(UUID roomId, MemberModifiedType type, Long userId);
}
