package com.connectcrew.teamone.compositeservice.composite.adapter.out.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.User;
import com.connectcrew.teamone.compositeservice.global.enums.Role;
import com.connectcrew.teamone.compositeservice.global.enums.Social;

import java.time.OffsetDateTime;

public record UserResponse(
        Long id,
        String socialId,
        Social provider,
        String username,
        String email,
        Role role,
        OffsetDateTime createdDate,
        OffsetDateTime modifiedDate
) {
    public User toDomain() {
        return new User(id, socialId, provider, username, email, role, createdDate, modifiedDate);
    }
}
