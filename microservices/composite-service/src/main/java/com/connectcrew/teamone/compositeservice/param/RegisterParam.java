package com.connectcrew.teamone.compositeservice.param;

import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;

public record RegisterParam(
        String token,
        Social social,
        String username,
        String nickname,
        String profile,
        String email,
        boolean termsAgreement,
        boolean privacyAgreement,
        String fcm
) {
    public UserInputParam toUserInputParam(String socialId) {
        return new UserInputParam(
                socialId,
                social,
                username,
                nickname,
                profile,
                email,
                termsAgreement,
                privacyAgreement,
                fcm
        );
    }
}
