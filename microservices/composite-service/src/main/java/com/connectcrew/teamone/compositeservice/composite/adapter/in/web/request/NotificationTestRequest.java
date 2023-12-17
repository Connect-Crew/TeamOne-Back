package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.SendNotificationCommand;

public record NotificationTestRequest(
        String title,
        String body
) {

    public SendNotificationCommand toCommand(Long userId) {
        return new SendNotificationCommand(userId, title(), body());
    }
}
