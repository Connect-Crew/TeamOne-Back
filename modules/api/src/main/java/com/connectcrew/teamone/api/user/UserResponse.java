package com.connectcrew.teamone.api.user;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
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
