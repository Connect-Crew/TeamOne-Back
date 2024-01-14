package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.userservice.user.Role;
import com.connectcrew.teamone.api.userservice.user.Social;
import com.connectcrew.teamone.api.userservice.user.UserApiResponse;

import java.time.LocalDateTime;

public record User(
        Long id,
        String socialId,
        Social provider,
        String username,
        String email,
        Role role,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate
) {

    public static User of(UserApiResponse res) {
        return new User(
                res.id(),
                res.socialId(),
                res.provider(),
                res.username(),
                res.email(),
                res.role(),
                res.createdDate(),
                res.modifiedDate()
        );
    }
}
