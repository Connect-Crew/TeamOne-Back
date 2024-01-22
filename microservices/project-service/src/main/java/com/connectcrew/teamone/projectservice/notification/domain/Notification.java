package com.connectcrew.teamone.projectservice.notification.domain;

import com.connectcrew.teamone.api.userservice.notification.push.SendMessageEvent;

public record Notification(
        Long userId,
        String title,
        String body,
        String deepLink
) {
    public SendMessageEvent toEvent() {
        return new SendMessageEvent(userId, title, body, deepLink);
    }
}
