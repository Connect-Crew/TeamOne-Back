package com.connectcrew.teamone.compositeservice.auth.domain;

import com.connectcrew.teamone.api.userservice.user.Role;
import lombok.Builder;

@Builder
public record TokenClaim(
        String socialId,
        Role role,
        Long id,
        String nickname
) {
}
