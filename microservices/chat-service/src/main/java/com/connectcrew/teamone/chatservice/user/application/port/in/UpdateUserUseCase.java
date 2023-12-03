package com.connectcrew.teamone.chatservice.user.application.port.in;

import com.connectcrew.teamone.chatservice.chatroom.domain.enums.MemberModifiedType;
import com.connectcrew.teamone.chatservice.user.domain.User;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UpdateUserUseCase {
    void updateUserSessionIfNotExists(Long userId, WebSocketSession session);

    void updateUser(User user);

    void addUsersChatRoomJoin(List<User> users);

    /**
     * 주어진 사용자들에게 채팅방 가입 정보를 추가하는 기능
     */
    void addUsersChatRoomJoin(UUID id, Set<Long> users);

    void addUsersChatRoomJoinOnSession(UUID id, Set<Long> users);

    void updateUserMemberOnSession(UUID roomId, MemberModifiedType type, Long userId);

}
