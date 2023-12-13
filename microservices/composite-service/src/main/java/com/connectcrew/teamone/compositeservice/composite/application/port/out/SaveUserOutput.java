package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.compositeservice.composite.domain.Register;
import reactor.core.publisher.Mono;

public interface SaveUserOutput {
    Mono<User> save(Register register);
}
