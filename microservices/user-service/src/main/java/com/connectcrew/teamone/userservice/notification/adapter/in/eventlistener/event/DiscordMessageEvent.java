package com.connectcrew.teamone.userservice.notification.adapter.in.eventlistener.event;

import com.connectcrew.teamone.userservice.notification.application.port.in.command.DiscordMessageCommand;
import com.connectcrew.teamone.userservice.notification.domain.DiscordChannel;
import com.connectcrew.teamone.userservice.notification.domain.MessageLevel;

public record DiscordMessageEvent(
        MessageLevel level,
        DiscordChannel channel,
        String caller,
        String title,
        String message
) {
    public DiscordMessageCommand toCommand() {
        return new DiscordMessageCommand(level, channel, caller, title, message);
    }
}
