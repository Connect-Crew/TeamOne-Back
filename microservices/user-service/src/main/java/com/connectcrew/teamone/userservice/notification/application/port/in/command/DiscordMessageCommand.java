package com.connectcrew.teamone.userservice.notification.application.port.in.command;

import com.connectcrew.teamone.api.userservice.notification.error.ErrorLevel;
import com.connectcrew.teamone.api.userservice.notification.error.ErrorNotification;
import com.connectcrew.teamone.api.userservice.notification.report.ReportNotification;
import com.connectcrew.teamone.userservice.notification.domain.DiscordChannel;
import com.connectcrew.teamone.userservice.notification.domain.DiscordMessage;

public record DiscordMessageCommand(
        ErrorLevel level,
        DiscordChannel channel,
        String title,
        String message
) {

    public static DiscordMessageCommand from(ErrorNotification event) {
        return new DiscordMessageCommand(event.level(), DiscordChannel.ERROR, String.format("%s\t%s", event.service(), event.caller()), event.message());
    }

    public static DiscordMessageCommand from(ReportNotification event, String sender) {
        return new DiscordMessageCommand(ErrorLevel.INFO, DiscordChannel.REPORT, String.format("reportId : %d\t\tuser : %s(id=%d)\t\tproject : %s(id=%d)", event.id(), sender, event.userId(), event.projectTitle(), event.projectId()), event.reason());
    }

    public DiscordMessage toDomain() {
        return new DiscordMessage(level, channel, title, message);
    }
}
