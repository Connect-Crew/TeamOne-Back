package com.connectcrew.teamone.userservice.user.adapter.in.web.response;

import com.connectcrew.teamone.userservice.user.domain.User;
import com.connectcrew.teamone.userservice.user.domain.enums.Role;
import com.connectcrew.teamone.userservice.user.domain.enums.Social;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
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

    public static UserResponse fromDomain(User user) {
        return UserResponse.builder()
                .id(user.id())
                .socialId(user.socialId())
                .provider(user.provider())
                .username(user.username())
                .email(user.email())
                .role(user.role())
                .createdDate(user.createdDate())
                .modifiedDate(user.modifiedDate())
                .build();
    }

}
