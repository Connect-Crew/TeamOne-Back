package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.user.notification.FcmNotification;
import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.param.NotificationTestParam;
import com.connectcrew.teamone.compositeservice.resposne.BooleanValueRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationTestController {

    private final JwtProvider jwtProvider;
    private final NotificationRequest notificationRequest;

    @PostMapping
    public Mono<BooleanValueRes> sendNotification(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody NotificationTestParam param) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return notificationRequest.sendNotification(new FcmNotification(id, param.title(), param.body()))
                .map(BooleanValueRes::new);
    }
}
