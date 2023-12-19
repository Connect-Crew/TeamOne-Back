package com.connectcrew.teamone.userservice.notification.adapter.in.eventlistener.event;

import com.connectcrew.teamone.userservice.notification.application.port.in.command.SendMessageCommand;

public record SendMessageEvent(
        Long userId,
        String title,
        String body,
        String deepLink
) {
    public SendMessageCommand toCommand() {
        return new SendMessageCommand(userId, title, body, deepLink);
    }
}
