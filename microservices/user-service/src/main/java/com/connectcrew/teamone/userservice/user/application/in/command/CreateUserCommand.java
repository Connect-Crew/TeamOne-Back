package com.connectcrew.teamone.userservice.user.application.in.command;


import com.connectcrew.teamone.api.user.Role;
import com.connectcrew.teamone.api.user.Social;
import com.connectcrew.teamone.api.user.UserRegisterRequest;
import com.connectcrew.teamone.userservice.notification.domain.FcmToken;
import com.connectcrew.teamone.userservice.profile.domain.Profile;
import com.connectcrew.teamone.userservice.user.domain.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateUserCommand(
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

    public static CreateUserCommand from(UserRegisterRequest request) {
        return CreateUserCommand.builder()
                .socialId(request.socialId())
                .provider(request.provider())
                .username(request.username())
                .nickname(request.nickname())
                .profile(request.profile())
                .email(request.email())
                .termsAgreement(request.termsAgreement())
                .privacyAgreement(request.privacyAgreement())
                .fcm(request.fcm())
                .build();
    }

    public User toUserDomain() {
        return User.builder()
                .socialId(socialId)
                .provider(provider)
                .username(username)
                .email(email)
                .role(Role.USER)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public Profile toProfileDomain(Long id) {
        return Profile.builder()
                .id(id)
                .nickname(nickname)
                .profile(profile)
                .introduction("")
                .temperature(36.5)
                .recvApply(0)
                .resApply(0)
                .build();
    }

    public FcmToken toFcmTokenDomain(Long id) {
        return FcmToken.builder()
                .user(id)
                .token(fcm)
                .build();
    }
}
