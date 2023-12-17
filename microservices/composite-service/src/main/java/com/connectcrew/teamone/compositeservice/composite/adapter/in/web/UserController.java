package com.connectcrew.teamone.compositeservice.composite.adapter.in.web;

import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.auth.domain.JwtToken;
import com.connectcrew.teamone.compositeservice.auth.domain.TokenClaim;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.LoginRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.RegisterRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.LoginResponse;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.ProfileResponse;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.RefreshResponse;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.AuthUserUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.QueryProfileUseCase;
import com.connectcrew.teamone.compositeservice.global.enums.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final JwtProvider jwtProvider;
    private final AuthUserUseCase authUserUseCase;
    private final QueryProfileUseCase queryProfileUseCase;

    @PostConstruct
    public void init() {
        JwtToken token = jwtProvider.createToken("123456", 2L, "TestUser", Role.USER);
        log.debug("Test token for test : {}", token != null ? token.accessToken() : null);
    }

    @PostMapping("/auth/login")
    public Mono<LoginResponse> login(@RequestBody LoginRequest request) {
        return authUserUseCase.login(request.token(), request.social(), request.fcm())
                .map(LoginResponse::from);
    }

    @PostMapping("/auth/register")
    public Mono<LoginResponse> register(@RequestBody RegisterRequest request) {
        return authUserUseCase.register(request.toCommand())
                .map(LoginResponse::from);
    }

    @PostMapping("/auth/refresh")
    public RefreshResponse refresh(@RequestHeader(JwtProvider.AUTH_HEADER) String token) {
        return RefreshResponse.from(authUserUseCase.refresh(token));
    }

    @GetMapping("/user/myprofile")
    public Mono<ProfileResponse> getMyProfile(@RequestHeader(JwtProvider.AUTH_HEADER) String token) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);

        return queryProfileUseCase.getFullProfile(claim.id())
                .map(ProfileResponse::from);
    }
}
