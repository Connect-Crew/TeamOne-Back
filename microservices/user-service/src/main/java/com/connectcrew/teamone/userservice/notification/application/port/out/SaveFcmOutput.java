package com.connectcrew.teamone.userservice.notification.application.port.out;

import com.connectcrew.teamone.userservice.notification.domain.FcmToken;
import reactor.core.publisher.Mono;

public interface SaveFcmOutput {
    Mono<FcmToken> save(FcmToken token);
}
