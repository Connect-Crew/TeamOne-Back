package com.connectcrew.teamone.userservice.notification.application.port.in.command;

import com.connectcrew.teamone.api.notification.error.ErrorNotification;
import com.connectcrew.teamone.userservice.notification.domain.DiscordChannel;
import com.connectcrew.teamone.userservice.notification.domain.DiscordMessage;
import com.connectcrew.teamone.api.notification.error.ErrorLevel;

public record DiscordMessageCommand(
        ErrorLevel level,
        DiscordChannel channel,
        String title,
        String message
) {

    public static DiscordMessageCommand from(ErrorNotification event) {
        return new DiscordMessageCommand(event.level(), DiscordChannel.ERROR, String.format("%s\t%s", event.service(), event.caller()), event.message());
    }

    public DiscordMessage toDomain() {
        return new DiscordMessage(level, channel, title, message);
    }
}
