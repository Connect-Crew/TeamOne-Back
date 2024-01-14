package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.api.userservice.user.UserRegisterApiRequest;
import com.connectcrew.teamone.compositeservice.composite.domain.User;
import reactor.core.publisher.Mono;

public interface SaveUserOutput {
    Mono<User> save(UserRegisterApiRequest register);
}
