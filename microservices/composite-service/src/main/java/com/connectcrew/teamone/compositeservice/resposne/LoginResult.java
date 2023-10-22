package com.connectcrew.teamone.compositeservice.resposne;

public record LoginResult(
        String token,
        String refreshToken,
        String nickname,
        String email,
        boolean isNewUser
) {
}
