package com.connectcrew.teamone.userservice.notification.application.port.out;

import com.connectcrew.teamone.userservice.notification.domain.DiscordMessage;

public interface SendDiscordOutput {
    boolean sendMessage(DiscordMessage message);
}
