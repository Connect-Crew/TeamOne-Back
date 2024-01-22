package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.api.userservice.user.Social;
import com.connectcrew.teamone.compositeservice.auth.domain.JwtToken;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.RegisterCommand;
import com.connectcrew.teamone.compositeservice.composite.domain.vo.LoginResult;
import reactor.core.publisher.Mono;

public interface AuthUserUseCase {

    Mono<LoginResult> login(String auth2Token, Social social, String fcmToken);

    Mono<LoginResult> register(RegisterCommand command);

    JwtToken refresh(String refreshToken);
}
