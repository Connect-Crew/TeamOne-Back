package com.connectcrew.teamone.userservice.user.adapter.in.web.request;

import com.connectcrew.teamone.userservice.user.application.in.command.CreateUserCommand;
import com.connectcrew.teamone.userservice.user.domain.enums.Social;

public record CreateUserRequest(
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

    public CreateUserCommand toCommand()  {
        return CreateUserCommand.builder()
                .socialId(socialId)
                .provider(provider)
                .username(username)
                .nickname(nickname)
                .profile(profile)
                .email(email)
                .termsAgreement(termsAgreement)
                .privacyAgreement(privacyAgreement)
                .fcm(fcm)
                .build();
    }
}
