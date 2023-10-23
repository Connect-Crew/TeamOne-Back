package com.connectcrew.teamone.compositeservice.resposne;

import lombok.Builder;

@Builder
public record LoginResult(
        String token,
        String refreshToken,
        String nickname,
        String profile,
        String email,
        boolean isNewUser
) {
}
