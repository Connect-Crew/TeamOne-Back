package com.connectcrew.teamone.projectservice.notification.domain;

public record Notification(
        Long userId,
        String title,
        String body,
        String deepLink
) {
}
