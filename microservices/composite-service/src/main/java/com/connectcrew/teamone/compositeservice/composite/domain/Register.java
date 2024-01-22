package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.userservice.user.Social;
import lombok.Builder;

@Builder
public record Register(
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
