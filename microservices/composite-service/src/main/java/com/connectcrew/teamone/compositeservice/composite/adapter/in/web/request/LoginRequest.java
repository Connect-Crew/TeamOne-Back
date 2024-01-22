package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;


import com.connectcrew.teamone.api.userservice.user.Social;

public record LoginRequest(
        String token,
        Social social,
        String fcm
) {
}
