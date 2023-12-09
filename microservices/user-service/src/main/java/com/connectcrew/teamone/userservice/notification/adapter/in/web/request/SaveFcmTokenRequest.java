package com.connectcrew.teamone.userservice.notification.adapter.in.web.request;

public record SaveFcmTokenRequest(
        Long id,
        String fcm
) {
}
