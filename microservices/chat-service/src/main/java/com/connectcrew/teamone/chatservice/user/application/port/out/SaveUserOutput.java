package com.connectcrew.teamone.chatservice.user.application.port.out;

import com.connectcrew.teamone.chatservice.user.domain.User;

import java.util.List;

public interface SaveUserOutput {
    User save(User user);

    List<User> saveAll(List<User> users);

}
