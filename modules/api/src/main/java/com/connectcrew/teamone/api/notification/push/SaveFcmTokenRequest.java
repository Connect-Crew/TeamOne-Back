package com.connectcrew.teamone.api.notification.push;

public record SaveFcmTokenRequest(
        Long id,
        String fcm
) {
}
