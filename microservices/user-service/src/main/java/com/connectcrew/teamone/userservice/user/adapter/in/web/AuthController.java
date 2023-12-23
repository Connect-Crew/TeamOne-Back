package com.connectcrew.teamone.userservice.user.adapter.in.web;

import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.userservice.notification.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.userservice.notification.domain.ErrorLevel;
import com.connectcrew.teamone.userservice.user.adapter.in.web.request.CreateUserRequest;
import com.connectcrew.teamone.userservice.user.adapter.in.web.response.UserResponse;
import com.connectcrew.teamone.userservice.user.application.in.CreateUserUseCase;
import com.connectcrew.teamone.userservice.user.application.in.QueryUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {

    private final SendErrorNotificationUseCase sendErrorNotificationUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final QueryUserUseCase queryUserUseCase;


    @GetMapping("/")
    public Mono<UserResponse> find(String socialId, Social provider) {
        return queryUserUseCase.findBySocialIdAndProvider(socialId, provider.name())
                .map(UserResponse::fromDomain)
                .doOnError(ex -> sendErrorNotificationUseCase.send("AuthController.find", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/")
    public Mono<UserResponse> save(@RequestBody CreateUserRequest request) {
        return createUserUseCase.create(request.toCommand())
                .map(UserResponse::fromDomain)
                .doOnError(ex -> sendErrorNotificationUseCase.send("AuthController.save", ErrorLevel.ERROR, ex));
    }
}
