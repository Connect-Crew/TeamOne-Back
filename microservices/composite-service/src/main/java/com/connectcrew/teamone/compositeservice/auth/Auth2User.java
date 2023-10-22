package com.connectcrew.teamone.compositeservice.auth;

import com.connectcrew.teamone.api.user.auth.Social;

public record Auth2User(
        String socialId,
        String email,
        String name,
        String profile,
        Social social
) {

}
