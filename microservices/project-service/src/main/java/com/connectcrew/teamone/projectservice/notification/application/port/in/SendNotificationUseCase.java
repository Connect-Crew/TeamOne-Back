package com.connectcrew.teamone.projectservice.notification.application.port.in;

import reactor.core.publisher.Mono;

public interface SendNotificationUseCase {
    Mono<Boolean> sendToLeader(Long userId, String title, String body, String deepLink);
}
