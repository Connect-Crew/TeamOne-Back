package com.connectcrew.teamone.userservice.notification.adapter.in.eventlistener.event;

import com.connectcrew.teamone.userservice.notification.application.port.in.command.DiscordMessageCommand;
import com.connectcrew.teamone.userservice.notification.domain.DiscordChannel;
import com.connectcrew.teamone.userservice.notification.domain.ErrorLevel;
import lombok.Builder;

@Builder
public record ErrorNotification(
        ErrorLevel level,
        String service,
        String caller,
        String message
) {
    public DiscordMessageCommand toCommand() {
        return new DiscordMessageCommand(level, DiscordChannel.ERROR, String.format("%s\t%s", service, caller), message);
    }

}

