package com.connectcrew.teamone.api.userservice.notification.push;

public record SendMessageEvent(
        Long userId,
        String title,
        String body,
        String deepLink
) {
}
