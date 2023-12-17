package com.connectcrew.teamone.compositeservice.composite.adapter.in.web;

import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.auth.domain.TokenClaim;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.NotificationTestRequest;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.SendNotificationUseCase;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.SimpleBooleanResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final JwtProvider jwtProvider;
    private final SendNotificationUseCase sendNotificationUseCase;

    @PostMapping
    public Mono<SimpleBooleanResponse> sendNotification(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody NotificationTestRequest request) {
        TokenClaim tokenClaim = jwtProvider.getTokenClaim(token);

        return sendNotificationUseCase.send(request.toCommand(tokenClaim.id()))
                .map(SimpleBooleanResponse::new);
    }
}
