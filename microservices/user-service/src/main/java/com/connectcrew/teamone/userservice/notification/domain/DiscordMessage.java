package com.connectcrew.teamone.userservice.notification.domain;

public record DiscordMessage(
        MessageLevel level,
        DiscordChannel channel,
        String caller,
        String title,
        String message
) {

    public String toMessage() {
        return String.format("%s - %s\n\n%s\n\n%s", level, title, message, caller);
    }

}