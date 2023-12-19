package com.connectcrew.teamone.userservice.notification.application.port.in;

import com.connectcrew.teamone.userservice.notification.application.port.in.command.DiscordMessageCommand;
import com.connectcrew.teamone.userservice.notification.application.port.in.command.SendMessageCommand;
import reactor.core.publisher.Mono;

public interface SendMessageUseCase {
    Mono<Boolean> sendMessage(SendMessageCommand command);

    Mono<Boolean> sendMessage(DiscordMessageCommand command);
}
