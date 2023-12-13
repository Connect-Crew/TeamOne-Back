package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

public record LoginRequest(
        String token,
        String social,
        String fcm
) {
}
