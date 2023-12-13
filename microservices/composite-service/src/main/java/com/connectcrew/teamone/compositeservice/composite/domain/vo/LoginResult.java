package com.connectcrew.teamone.compositeservice.composite.domain.vo;

import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.profile.Profile;
import com.connectcrew.teamone.compositeservice.auth.domain.JwtToken;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoginResult(
        Long id,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer responseRate,
        List<String> parts,
        String email,
        String token,
        LocalDateTime exp,
        String refreshToken,
        LocalDateTime refreshExp
) {
    public static LoginResult from(User user, Profile profile, JwtToken token) {
        return LoginResult.builder()
                .id(user.id())
                .nickname(profile.nickname())
                .profile(profile.profile())
                .introduction(profile.introduction())
                .temperature(profile.temperature())
                .responseRate(profile.responseRate())
                .parts(profile.parts())
                .email(user.email())
                .token(token.accessToken())
                .exp(token.accessTokenExp())
                .refreshToken(token.refreshToken())
                .refreshExp(token.refreshTokenExp())
                .build();
    }
}
