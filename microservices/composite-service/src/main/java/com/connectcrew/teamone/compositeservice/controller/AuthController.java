package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.compositeservice.auth.Auth2TokenValidator;
import com.connectcrew.teamone.compositeservice.auth.TokenGenerator;
import com.connectcrew.teamone.compositeservice.exception.UnauthorizedException;
import com.connectcrew.teamone.compositeservice.param.LoginParam;
import com.connectcrew.teamone.compositeservice.param.RegisterParam;
import com.connectcrew.teamone.compositeservice.request.UserRequest;
import com.connectcrew.teamone.compositeservice.resposne.LoginResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Auth2TokenValidator auth2TokenValidator;
    private final UserRequest userRequest;

    private final TokenGenerator tokenGenerator;

    @PostMapping("/login")
    public Mono<LoginResult> login(@RequestBody LoginParam param) {
        log.trace("login param={}", param);
        return auth2TokenValidator.validate(param.token(), param.social())
                .onErrorResume(ex -> Mono.error(new UnauthorizedException("유효하지 않은 Token 입니다.", ex)))
                .doOnError(ex -> log.debug("login error: {}", ex.getMessage(), ex))
                .flatMap(socialId -> userRequest.getUser(socialId, param.social()))
                .map(this::generateLoginResult);
    }

    @PostMapping("/register")
    public Mono<LoginResult> register(@RequestBody RegisterParam param) {
        log.trace("register param={}", param);
        return auth2TokenValidator.validate(param.token(), param.social())
                .onErrorResume(ex -> Mono.error(new UnauthorizedException("유효하지 않은 Token 입니다.", ex)))
                .doOnError(ex -> log.debug("register error: {}", ex.getMessage(), ex))
                .flatMap(socialId -> userRequest.saveUser(param.toUserInputParam(socialId)))
                .map(this::generateLoginResult);

    }

    private LoginResult generateLoginResult(User user) {
        String accessToken = tokenGenerator.createAccessToken(user.socialId(), user.role());
        String refreshToken = tokenGenerator.createRefreshToken(user.socialId(), user.role());

        return LoginResult.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .nickname(user.nickname())
                .email(user.email())
                .profile(user.profile())
                .build();
    }
}
