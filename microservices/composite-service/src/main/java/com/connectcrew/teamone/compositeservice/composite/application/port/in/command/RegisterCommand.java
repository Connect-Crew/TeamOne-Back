package com.connectcrew.teamone.compositeservice.composite.application.port.in.command;

import com.connectcrew.teamone.api.userservice.user.Social;
import com.connectcrew.teamone.api.userservice.user.UserRegisterApiRequest;
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

    public UserRegisterApiRequest toApiRequest(String socialId) {
        return new UserRegisterApiRequest(
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
