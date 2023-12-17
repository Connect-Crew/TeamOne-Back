package com.connectcrew.teamone.userservice.user.application.out;

import com.connectcrew.teamone.userservice.user.domain.User;
import reactor.core.publisher.Mono;

public interface SaveUserOutput {
    Mono<User> save(User user);

}
