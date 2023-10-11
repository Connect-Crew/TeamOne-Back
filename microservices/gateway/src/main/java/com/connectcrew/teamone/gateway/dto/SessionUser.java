package com.connectcrew.teamone.gateway.dto;

import com.connectcrew.teamone.api.user.auth.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private final String name;
    private final String nickname;
    private final String email;


    public SessionUser(User user) {
        this.nickname = user.nickname();
        this.name = user.username();
        this.email = user.email();
    }

}