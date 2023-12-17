package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import reactor.core.publisher.Mono;

public interface SaveNotificationOutput {
    Mono<Boolean> saveFcm(Long id, String fcm);
}
