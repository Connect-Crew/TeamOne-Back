package com.connectcrew.teamone.compositeservice.resposne;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoginResult(
        String token,
        LocalDateTime exp,
        String refreshToken,
        LocalDateTime refreshExp,
        String nickname,
        String profile,
        String email
) {
}
