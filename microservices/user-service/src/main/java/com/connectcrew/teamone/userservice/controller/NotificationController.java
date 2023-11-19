package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.user.notification.FcmNotification;
import com.connectcrew.teamone.api.user.notification.FcmToken;
import com.connectcrew.teamone.userservice.service.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final FcmNotificationService fcmService;

    @PostMapping
    public Mono<Boolean> sendNotification(@RequestBody FcmNotification notification) {
        return fcmService.sendNotification(notification);
    }

    @PostMapping("/token")
    public Mono<Boolean> saveToken(@RequestBody FcmToken token) {
        return fcmService.saveToken(token.id(), token.fcm());
    }
}
