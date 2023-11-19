package com.connectcrew.teamone.userservice.service;

import com.connectcrew.teamone.api.user.notification.FcmNotification;
import com.connectcrew.teamone.userservice.repository.FcmRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmNotificationService {
    private final FcmRepository fcmRepository;
    private final FirebaseMessaging firebaseMessaging;

    public Mono<Boolean> sendNotification(FcmNotification notification) {
        log.trace("sendNotification: {}", notification);
        return fcmRepository.findAllByUserId(notification.getUserId())
                .flatMap(entity -> {
                    try {
                        Notification noti = Notification.builder()
                                .setTitle(notification.getTitle())
                                .setBody(notification.getBody())
                                .build();

                        Message msg = Message.builder()
                                .setToken(entity.getToken())
                                .setNotification(noti)
                                .build();

                        log.trace("sendNotification - token: {}, title: {}, body: {}", entity.getToken(), notification.getTitle(), notification.getBody());

                        firebaseMessaging.send(msg);
                        return Mono.just(true);
                    } catch (FirebaseMessagingException e) {
                        log.trace("sendNotification - error: {}", e.getMessage(), e);
                        return fcmRepository.delete(entity)
                                .thenReturn(false);
                    }
                })
                .then()
                .thenReturn(true);
    }
}
