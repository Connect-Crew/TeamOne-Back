package com.connectcrew.teamone.userservice.notification.application.port.out;

import com.connectcrew.teamone.userservice.notification.domain.FcmToken;
import reactor.core.publisher.Flux;

public interface FindFcmOutput {
    Flux<FcmToken> findAllFcmTokenByUserId(Long userId);
}
