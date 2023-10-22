package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.compositeservice.auth.Auth2User;
import com.connectcrew.teamone.compositeservice.auth.TokenGenerator;
import com.connectcrew.teamone.compositeservice.auth.TokenResolver;
import com.connectcrew.teamone.compositeservice.exception.UnauthorizedException;
import com.connectcrew.teamone.compositeservice.request.UserRequest;
import com.connectcrew.teamone.compositeservice.resposne.LoginResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenResolver tokenResolver;
    private final UserRequest userRequest;

    private TokenGenerator tokenGenerator = new TokenGenerator() {

        @Override
        public String createToken(String account, Role role) {
            return "Access Token";
        }

        @Override
        public String createRefreshToken(String account, Role role) {
            return "Refresh Token";
        }
    };

    @GetMapping("/login")
    public Mono<LoginResult> login(String token, Social social) {
        log.trace("login token: {}, social: {}", token, social);
        return tokenResolver.resolve(token, social)
                .switchIfEmpty(Mono.error(new UnauthorizedException("유효하지 않은 Token 입니다.")))
                .flatMap(auth2User -> userRequest.getUser(auth2User.socialId(), auth2User.social()).map(user -> Tuples.of(user, false))
                        .switchIfEmpty(saveUser(auth2User).map(user -> Tuples.of(user, true))))
                .map(tuple -> generateLoginResult(tuple.getT1(), tuple.getT2()));
    }

    private LoginResult generateLoginResult(User user, boolean isNew) {
        String accessToken = tokenGenerator.createToken(user.socialId(), user.role());
        String refreshToken = tokenGenerator.createRefreshToken(user.socialId(), user.role());

        return new LoginResult(accessToken, refreshToken, user.nickname(), user.email(), isNew);
    }

    private Mono<User> saveUser(Auth2User auth2User) {
        return userRequest.saveUser(new UserInputParam(
                auth2User.socialId(),
                auth2User.social(),
                auth2User.name(),
                auth2User.name(),
                auth2User.email()
        ));
    }
}
