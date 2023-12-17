package com.connectcrew.teamone.compositeservice.composite.adapter.out.web.request;

import com.connectcrew.teamone.compositeservice.composite.domain.Register;
import com.connectcrew.teamone.compositeservice.global.enums.Social;

public record RegisterRequest(
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
    public static RegisterRequest from(Register register) {
        return new RegisterRequest(
                register.socialId(),
                register.provider(),
                register.username(),
                register.nickname(),
                register.profile(),
                register.email(),
                register.termsAgreement(),
                register.privacyAgreement(),
                register.fcm()
        );
    }
}
