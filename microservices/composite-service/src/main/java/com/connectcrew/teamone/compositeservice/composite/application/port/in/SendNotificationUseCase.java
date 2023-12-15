package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.SendNotificationCommand;
import reactor.core.publisher.Mono;

public interface SendNotificationUseCase {
    Mono<Boolean> send(SendNotificationCommand command);
}
