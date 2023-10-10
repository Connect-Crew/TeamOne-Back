package com.connectcrew.teamone.api.user.auth.param;

public record UserInputParam(
        String username,
        String password,
        String nickname,
        String email
) {
}
