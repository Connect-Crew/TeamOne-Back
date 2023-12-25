package com.connectcrew.teamone.userservice.user.domain;


import com.connectcrew.teamone.api.user.Role;
import com.connectcrew.teamone.api.user.Social;
import com.connectcrew.teamone.api.user.UserResponse;
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
        Boolean termsAgreement,
        Boolean privacyAgreement,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate
) {
    public UserResponse toResponse() {
        return UserResponse.builder()
                .id(id)
                .socialId(socialId)
                .provider(provider)
                .username(username)
                .email(email)
                .role(role)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .build();
    }

}