package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.global.enums.Role;
import com.connectcrew.teamone.compositeservice.global.enums.Social;

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
}
