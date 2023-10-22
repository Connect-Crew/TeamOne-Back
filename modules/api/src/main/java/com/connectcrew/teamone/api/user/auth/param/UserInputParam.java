package com.connectcrew.teamone.api.user.auth.param;

import com.connectcrew.teamone.api.user.auth.Social;

public record UserInputParam(
        String socialId,
        Social provider,
        String username,
        String nickname,
        String email
) {
}
