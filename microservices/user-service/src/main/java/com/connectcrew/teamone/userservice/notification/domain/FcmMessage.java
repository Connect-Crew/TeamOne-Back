package com.connectcrew.teamone.userservice.notification.domain;

import lombok.Builder;

@Builder
public record FcmMessage(
        String fcm,
        String title,
        MessageBody body,
        String deepLink
) {
}
