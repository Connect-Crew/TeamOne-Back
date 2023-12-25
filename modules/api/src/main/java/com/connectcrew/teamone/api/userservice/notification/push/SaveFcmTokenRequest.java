package com.connectcrew.teamone.api.userservice.notification.push;

public record SaveFcmTokenRequest(
        Long id,
        String fcm
) {
}
