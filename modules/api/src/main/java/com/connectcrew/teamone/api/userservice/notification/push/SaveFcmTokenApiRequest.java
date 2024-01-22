package com.connectcrew.teamone.api.userservice.notification.push;

public record SaveFcmTokenApiRequest(
        Long id,
        String fcm
) {
}
