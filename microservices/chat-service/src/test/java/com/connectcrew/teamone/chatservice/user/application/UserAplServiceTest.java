package com.connectcrew.teamone.chatservice.user.application;

import com.connectcrew.teamone.chatservice.user.application.port.out.FindUserOutput;
import com.connectcrew.teamone.chatservice.user.application.port.out.SaveUserOutput;
import com.connectcrew.teamone.chatservice.user.domain.User;
import com.connectcrew.teamone.chatservice.user.domain.vo.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class UserAplServiceTest {

    private FindUserOutput findUserOutput;
    private SaveUserOutput saveUserOutput;
    private UserAplService service;

    @BeforeEach
    void setup() {
        findUserOutput = Mockito.mock(FindUserOutput.class);
        saveUserOutput = Mockito.mock(SaveUserOutput.class);
        service = new UserAplService(findUserOutput, saveUserOutput);
    }

    @Test
    void updateUserSessionIfNotExists() {
        when(findUserOutput.findById(anyLong())).thenReturn(Optional.empty());

        Optional<UserSession> session = service.findUserSessionByUserId(0L);

        assertThat(session.isPresent()).isFalse();

        service.updateUserSessionIfNotExists(0L, Mockito.mock(WebSocketSession.class));

        session = service.findUserSessionByUserId(0L);

        assertThat(session.isPresent()).isTrue();
    }

    @Test
    void findAllUserSessionByRoomId() {
        when(findUserOutput.findById(anyLong())).thenReturn(Optional.empty());
        when(saveUserOutput.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        service.updateUserSessionIfNotExists(0L, Mockito.mock(WebSocketSession.class));
        service.updateUserSessionIfNotExists(1L, Mockito.mock(WebSocketSession.class));
        service.updateUserSessionIfNotExists(2L, Mockito.mock(WebSocketSession.class));

        UUID roomId = UUID.randomUUID();
        service.updateUser(new User(0L, Set.of(roomId)));
        service.updateUser(new User(1L, Set.of(roomId)));

        assertThat(service.findAllUserSessionByRoomId(roomId).size()).isEqualTo(2);
    }
}
