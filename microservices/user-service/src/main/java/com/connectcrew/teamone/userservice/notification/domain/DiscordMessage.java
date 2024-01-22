package com.connectcrew.teamone.userservice.notification.domain;

import com.connectcrew.teamone.api.userservice.notification.error.ErrorLevel;

public record DiscordMessage(
        ErrorLevel level,
        DiscordChannel channel,
        String title,
        String message
) {

    public String toMessage() {
        return String.format("### [%s]\t%s\n```%s```\n\n", level, title, message);
    }

}