package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.global.enums.Role;
import com.connectcrew.teamone.compositeservice.global.enums.Social;

import java.time.OffsetDateTime;

public record User(
        Long id,
        String socialId,
        Social provider,
        String username,
        String email,
        Role role,
        OffsetDateTime createdDate,
        OffsetDateTime modifiedDate
) {
}
