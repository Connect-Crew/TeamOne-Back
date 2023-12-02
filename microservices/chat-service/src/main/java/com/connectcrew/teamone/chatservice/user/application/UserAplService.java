package com.connectcrew.teamone.chatservice.user.application;

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
    public void updateUser(User user) {
        user = saveUserOutput.save(user);

        if (!userSessions.containsKey(user.id())) return;
        userSessions.put(user.id(), new UserSession(user, userSessions.get(user.id()).session()));
    }

    @Override
    public void updateAll(List<User> users) {
        users = saveUserOutput.saveAll(users);

        for (User user : users) {
            if (!userSessions.containsKey(user.id())) continue;
            userSessions.put(user.id(), new UserSession(user, userSessions.get(user.id()).session()));
        }
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
