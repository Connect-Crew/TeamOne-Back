package com.connectcrew.teamone.userservice.notification.application.port.in.command;

import com.connectcrew.teamone.userservice.notification.domain.DiscordChannel;
import com.connectcrew.teamone.userservice.notification.domain.DiscordMessage;
import com.connectcrew.teamone.userservice.notification.domain.ErrorLevel;

public record DiscordMessageCommand(
        ErrorLevel level,
        DiscordChannel channel,
        String title,
        String message
) {

    public DiscordMessage toDomain() {
        return new DiscordMessage(level, channel, title, message);
    }
}
