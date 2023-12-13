package com.connectcrew.teamone.compositeservice.composite.application.port.in.command;

import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.compositeservice.composite.domain.Register;

public record RegisterCommand(
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
    public Register toDomain(String socialId) {
        return new Register(
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
