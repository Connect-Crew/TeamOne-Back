package com.connectcrew.teamone.userservice.notification.adapter.in.web;

import com.connectcrew.teamone.userservice.notification.adapter.in.web.request.SaveFcmTokenRequest;
import com.connectcrew.teamone.userservice.notification.application.port.in.SaveFcmTokenUseCase;
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

    private final SaveFcmTokenUseCase saveFcmTokenUseCase;

    @PostMapping("/token")
    public Mono<Boolean> saveToken(@RequestBody SaveFcmTokenRequest request) {
        return saveFcmTokenUseCase.saveFcmToken(request.id(), request.fcm());
    }
}
