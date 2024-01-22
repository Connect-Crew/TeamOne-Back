package com.connectcrew.teamone.api.userservice.user;

import lombok.Builder;

@Builder
public record UserRegisterApiRequest(
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
