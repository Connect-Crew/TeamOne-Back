package com.connectcrew.teamone.api.user.auth;

import lombok.Builder;

@Builder
public record User(
        Long id,
        String socialId,
        Social provider,
        String username,
        String nickname,
        String email,
        Role role,
        String createdDate,
        String modifiedDate
) {

}
