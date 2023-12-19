package com.connectcrew.teamone.userservice.notification.application.port.in.command;

import com.connectcrew.teamone.userservice.notification.domain.DiscordChannel;
import com.connectcrew.teamone.userservice.notification.domain.DiscordMessage;
import com.connectcrew.teamone.userservice.notification.domain.MessageLevel;

public record DiscordMessageCommand(
        MessageLevel level,
        DiscordChannel channel,
        String caller,
        String title,
        String message
) {

    public DiscordMessage toDomain() {
        return new DiscordMessage(level, channel, caller, title, message);
    }
}
