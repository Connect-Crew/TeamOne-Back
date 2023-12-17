package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.compositeservice.composite.domain.Register;
import com.connectcrew.teamone.compositeservice.composite.domain.User;
import reactor.core.publisher.Mono;

public interface SaveUserOutput {
    Mono<User> save(Register register);
}
