package com.connectcrew.teamone.chatservice.user.application;

import com.connectcrew.teamone.chatservice.chatroom.domain.enums.MemberModifiedType;
import com.connectcrew.teamone.chatservice.user.application.port.in.DeleteUserUseCase;
import com.connectcrew.teamone.chatservice.user.application.port.in.QueryUserUseCase;
import com.connectcrew.teamone.chatservice.user.application.port.in.UpdateUserUseCase;
import com.connectcrew.teamone.chatservice.user.application.port.out.FindUserOutput;
import com.connectcrew.teamone.chatservice.user.application.port.out.SaveUserOutput;
import com.connectcrew.teamone.chatservice.user.domain.User;
import com.connectcrew.teamone.chatservice.user.domain.vo.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserAplService implements UpdateUserUseCase, QueryUserUseCase, DeleteUserUseCase {
    private final Map<Long, UserSession> userSessions;

    private final FindUserOutput findUserOutput;
    private final SaveUserOutput saveUserOutput;

    public UserAplService(FindUserOutput findUserOutput, SaveUserOutput saveUserOutput) {
        this.userSessions = new HashMap<>();
        this.findUserOutput = findUserOutput;
        this.saveUserOutput = saveUserOutput;
    }

    @Override
    public void updateUserSessionIfNotExists(Long userId, WebSocketSession session) {
        if (userSessions.containsKey(userId)) return;

        User user = findUserOutput.findById(userId).orElse(new User(userId, new HashSet<>()));

        userSessions.put(userId, new UserSession(user, session));
    }

    @Override
    public Optional<UserSession> findUserSessionByUserId(Long userId) {
        return Optional.ofNullable(userSessions.get(userId));
    }

    @Override
    public List<UserSession> findAllUserSessionByRoomId(UUID roomId) {
        return userSessions.values().stream()
                .filter(u -> u.isJoined(roomId))
                .toList();
    }

    @Override
    public void updateUser(User user) {
        user = saveUserOutput.save(user);

        if (!userSessions.containsKey(user.id())) return;
        userSessions.put(user.id(), new UserSession(user, userSessions.get(user.id()).session()));
    }

    @Override
    public void addUsersChatRoomJoin(UUID id, Set<Long> userIds) {
        List<User> users = getUsersIfNotExistCreateUser(userIds);
        users.forEach(user -> user.addChatRoom(id));
        saveUserOutput.saveAll(users);
    }

    @Override
    public void addUsersChatRoomJoinOnSession(UUID id, Set<Long> users) {
        for(Long userId : users) {
            if (!userSessions.containsKey(userId)) continue;
            userSessions.get(userId).user().addChatRoom(id);
        }
    }

    @Override
    public void updateUserMemberOnSession(UUID roomId, MemberModifiedType type, Long userId) {
        if (!userSessions.containsKey(userId)) return;
        User user = userSessions.get(userId).user();
        if (type == MemberModifiedType.JOIN) user.addChatRoom(roomId);
        else user.removeChatRoom(roomId);
    }

    /**
     * 주어진 ID의 User들을 불러오는 함수.
     * 이때, User가 DB에 존재하지 않으면 새로 생성한다.
     */
    private List<User> getUsersIfNotExistCreateUser(Set<Long> userIds) {
        List<User> users = findUserOutput.findAllByIds(userIds);
        HashSet<Long> userIdsSet = users.stream().map(User::id).collect(Collectors.toCollection(HashSet::new));
        for (Long userId : userIds) {
            if (userIdsSet.contains(userId)) continue;

            users.add(new User(userId, new HashSet<>()));
        }
        return users;
    }

    @Override
    public void deleteUserSession(WebSocketSession session) {
        Optional<UserSession> optionalUser = userSessions.values().stream()
                .filter(u -> u.session().equals(session))
                .findFirst();

        if (optionalUser.isEmpty()) {
            log.trace("not found websocket session user");
            return;
        }
        UserSession userSession = optionalUser.get();
        userSessions.remove(userSession.user().id());

        log.trace("remove websocket session user: {}", userSession.user().id());
    }
}
