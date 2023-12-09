package com.connectcrew.teamone.userservice.notification.application.port.in;

import com.connectcrew.teamone.userservice.notification.application.port.in.command.SendMessageCommand;
import reactor.core.publisher.Mono;

public interface SendMessageUseCase {
    Mono<Boolean> sendMessage(SendMessageCommand command);
}
