package com.connectcrew.teamone.userservice.user.application.in.command;


import com.connectcrew.teamone.userservice.profile.domain.Profile;
import com.connectcrew.teamone.userservice.notification.domain.FcmToken;
import com.connectcrew.teamone.userservice.user.domain.User;
import com.connectcrew.teamone.userservice.user.domain.enums.Role;
import com.connectcrew.teamone.userservice.user.domain.enums.Social;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.ArrayList;

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
    public User toUserDomain() {
        return User.builder()
                .socialId(socialId)
                .provider(provider)
                .username(username)
                .email(email)
                .role(Role.USER)
                .createdDate(OffsetDateTime.now())
                .modifiedDate(OffsetDateTime.now())
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
                .representProjects(new ArrayList<>())
                .build();
    }

    public FcmToken toFcmTokenDomain(Long id) {
        return new FcmToken(id, fcm);
    }
}
