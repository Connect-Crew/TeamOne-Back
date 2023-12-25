package com.connectcrew.teamone.userservice.user.application.out;

import com.connectcrew.teamone.api.userservice.user.Social;
import com.connectcrew.teamone.userservice.user.domain.User;
import reactor.core.publisher.Mono;

public interface FindUserOutput {
    Mono<Boolean>  existsBySocialIdAndProvider(String socialId, Social provider);

    Mono<User> findBySocialIdAndProvider(String socialId, Social provider);
}
