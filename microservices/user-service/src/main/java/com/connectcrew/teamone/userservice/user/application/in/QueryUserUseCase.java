package com.connectcrew.teamone.userservice.user.application.in;

import com.connectcrew.teamone.userservice.user.domain.User;
import reactor.core.publisher.Mono;

public interface QueryUserUseCase {
    Mono<User> findBySocialIdAndProvider(String socialId, String provider);
}
