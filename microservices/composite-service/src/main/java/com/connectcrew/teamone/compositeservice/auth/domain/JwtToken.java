package com.connectcrew.teamone.compositeservice.auth.domain;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record JwtToken(
        String accessToken,
        OffsetDateTime accessTokenExp,
        String refreshToken,
        OffsetDateTime refreshTokenExp
) {
}
