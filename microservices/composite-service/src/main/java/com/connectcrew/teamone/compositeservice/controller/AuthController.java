package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.compositeservice.auth.Auth2TokenValidator;
import com.connectcrew.teamone.compositeservice.auth.JwtProvider;
import com.connectcrew.teamone.compositeservice.exception.UnauthorizedException;
import com.connectcrew.teamone.compositeservice.param.LoginParam;
import com.connectcrew.teamone.compositeservice.param.RegisterParam;
import com.connectcrew.teamone.compositeservice.request.UserRequest;
import com.connectcrew.teamone.compositeservice.resposne.LoginResult;
import com.connectcrew.teamone.compositeservice.resposne.RefreshResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Auth2TokenValidator auth2TokenValidator;
    private final UserRequest userRequest;

    private final JwtProvider jwtProvider;

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
        LocalDateTime now = LocalDateTime.now();
        String accessToken = jwtProvider.createAccessToken(user.socialId(), user.role());
        LocalDateTime accessExp = now.plusSeconds(JwtProvider.accessExp / 1000);

        String refreshToken = jwtProvider.createRefreshToken(user.socialId(), user.role());
        LocalDateTime refreshExp = now.plusSeconds(JwtProvider.refreshExp / 1000);

        return LoginResult.builder()
                .token(accessToken)
                .exp(accessExp)
                .refreshToken(refreshToken)
                .refreshExp(refreshExp)
                .nickname(user.nickname())
                .email(user.email())
                .profile(user.profile())
                .build();
    }

    @PostMapping("/refresh")
    public RefreshResult refresh(@RequestHeader(JwtProvider.AUTH_HEADER) String token) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");

        String account = jwtProvider.getAccount(removedPrefix);
        Role role = jwtProvider.getRole(removedPrefix);

        String accessToken = jwtProvider.createAccessToken(account, role);
        LocalDateTime accessExp = LocalDateTime.now().plusSeconds(JwtProvider.accessExp / 1000);

        return RefreshResult.builder()
                .token(accessToken)
                .exp(accessExp)
                .build();
    }

}
