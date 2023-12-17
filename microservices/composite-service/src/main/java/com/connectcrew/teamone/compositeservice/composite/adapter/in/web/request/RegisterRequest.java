package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.RegisterCommand;
import com.connectcrew.teamone.compositeservice.global.enums.Social;

public record RegisterRequest(
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

    public RegisterCommand toCommand() {
        return new RegisterCommand(
                token,
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