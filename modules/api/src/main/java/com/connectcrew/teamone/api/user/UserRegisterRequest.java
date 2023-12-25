package com.connectcrew.teamone.api.user;

import lombok.Builder;

@Builder
public record UserRegisterRequest(
        String socialId,
        Social provider,
        String username,
        String nickname,
        String profile,
        String email,
        boolean termsAgreement,
        boolean privacyAgreement,
        String fcm
) {
}
