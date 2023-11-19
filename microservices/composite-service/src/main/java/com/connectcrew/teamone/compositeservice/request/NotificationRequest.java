package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.api.user.notification.FcmNotification;
import reactor.core.publisher.Mono;

public interface NotificationRequest {
    Mono<Boolean> sendNotification(FcmNotification notification);

    Mono<Boolean> saveFcm(Long id, String fcm);
}
