package com.connectcrew.teamone.api.user.auth;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
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

}
