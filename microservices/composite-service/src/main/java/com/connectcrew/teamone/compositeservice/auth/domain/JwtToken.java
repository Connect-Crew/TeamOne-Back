package com.connectcrew.teamone.compositeservice.auth.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record JwtToken(
        String accessToken,
        LocalDateTime accessTokenExp,
        String refreshToken,
        LocalDateTime refreshTokenExp
) {
}
