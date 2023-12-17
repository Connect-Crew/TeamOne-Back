package com.connectcrew.teamone.compositeservice.composite.application;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.SendNotificationUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.SendNotificationCommand;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.SendNotificationOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationAplService implements SendNotificationUseCase {

    private final SendNotificationOutput sendNotificationOutput;

    @Override
    public Mono<Boolean> send(SendNotificationCommand command) {
        sendNotificationOutput.send(command.toDomain());
        return Mono.just(true);
    }
}
