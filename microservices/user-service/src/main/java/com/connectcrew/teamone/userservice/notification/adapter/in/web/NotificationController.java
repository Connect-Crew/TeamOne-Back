package com.connectcrew.teamone.userservice.notification.adapter.in.web;

import com.connectcrew.teamone.api.notification.push.SaveFcmTokenRequest;
import com.connectcrew.teamone.userservice.notification.application.port.in.SaveFcmTokenUseCase;
import com.connectcrew.teamone.userservice.notification.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.api.notification.error.ErrorLevel;
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

    private final SendErrorNotificationUseCase sendErrorNotificationUseCase;
    private final SaveFcmTokenUseCase saveFcmTokenUseCase;

    @PostMapping("/token")
    public Mono<Boolean> saveToken(@RequestBody SaveFcmTokenRequest request) {
        return saveFcmTokenUseCase.saveFcmToken(request.id(), request.fcm())
                .doOnError(ex -> sendErrorNotificationUseCase.send("NotificationController.saveToken", ErrorLevel.ERROR, ex));
    }
}
