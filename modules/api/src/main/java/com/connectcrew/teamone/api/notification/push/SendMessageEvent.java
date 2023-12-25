package com.connectcrew.teamone.api.notification.push;

public record SendMessageEvent(
        Long userId,
        String title,
        String body,
        String deepLink
) {
}
