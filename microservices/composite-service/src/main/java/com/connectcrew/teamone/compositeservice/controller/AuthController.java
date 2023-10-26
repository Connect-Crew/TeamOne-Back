package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.compositeservice.auth.Auth2User;
import com.connectcrew.teamone.compositeservice.auth.TokenGenerator;
import com.connectcrew.teamone.compositeservice.auth.TokenResolver;
import com.connectcrew.teamone.compositeservice.exception.UnauthorizedException;
import com.connectcrew.teamone.compositeservice.param.LoginParam;
import com.connectcrew.teamone.compositeservice.param.RegisterParam;
import com.connectcrew.teamone.compositeservice.request.UserRequest;
import com.connectcrew.teamone.compositeservice.resposne.LoginResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenResolver tokenResolver;
    private final UserRequest userRequest;

    private final TokenGenerator tokenGenerator;

    @PostMapping("/login")
    public Mono<LoginResult> login(@RequestBody LoginParam param) {
        log.trace("login param={}", param);
        return tokenResolver.resolve(param.token(), param.social())
                .onErrorResume(ex -> Mono.error(new UnauthorizedException("유효하지 않은 Token 입니다.", ex)))
                .doOnError(ex -> log.debug("login error: {}", ex.getMessage(), ex))
                .flatMap(auth2User -> userRequest.getUser(auth2User.socialId(), auth2User.social()))
                .map(this::generateLoginResult);
    }

    @PostMapping("/register")
    public Mono<LoginResult> register(@RequestBody RegisterParam param) {
        log.trace("register param={}", param);
        return tokenResolver.resolve(param.token(), param.social())
                .onErrorResume(ex -> Mono.error(new UnauthorizedException("유효하지 않은 Token 입니다.", ex)))
                .doOnError(ex -> log.debug("register error: {}", ex.getMessage(), ex))
                .flatMap(auth2User -> userRequest.saveUser(userToParam(auth2User, param)))
                .map(this::generateLoginResult);

    }

    private UserInputParam userToParam(Auth2User auth2User, RegisterParam param) {
        return UserInputParam.builder()
                .socialId(auth2User.socialId())
                .provider(auth2User.social())
                .username(auth2User.name())
                .nickname(param.name())
                .profile(auth2User.profile())
                .email(auth2User.email())
                .termsAgreement(param.termsAgreement())
                .privacyAgreement(param.privacyAgreement())
                .communityPolicyAgreement(param.communityPolicyAgreement())
                .adNotificationAgreement(param.adNotificationAgreement())
                .build();
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
