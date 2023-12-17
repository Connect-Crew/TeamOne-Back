package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import com.connectcrew.teamone.compositeservice.auth.domain.JwtToken;

import java.time.LocalDateTime;

public record RefreshResponse(
        String token,
        LocalDateTime exp,
        String refresh,
        LocalDateTime refreshExp
) {
    public static RefreshResponse from(JwtToken token) {
        return new RefreshResponse(
                token.accessToken(),
                token.accessTokenExp(),
                token.refreshToken(),
                token.refreshTokenExp()
        );
    }
}
