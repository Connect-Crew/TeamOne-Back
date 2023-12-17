package com.connectcrew.teamone.userservice.notification.application.port.out;

import com.connectcrew.teamone.userservice.notification.domain.FcmToken;
import reactor.core.publisher.Mono;

public interface DeleteFcmOutput {
    Mono<Boolean> deleteFcmToken(FcmToken token);
}
