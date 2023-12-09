package com.connectcrew.teamone.userservice.notification.application.port.in;

import reactor.core.publisher.Mono;

public interface SaveFcmTokenUseCase {
    Mono<Boolean> saveFcmToken(Long user, String token);
}
