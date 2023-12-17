package com.connectcrew.teamone.userservice.user.application.out;

import com.connectcrew.teamone.userservice.user.domain.User;
import com.connectcrew.teamone.userservice.user.domain.enums.Social;
import reactor.core.publisher.Mono;

public interface FindUserOutput {
    Mono<Boolean>  existsBySocialIdAndProvider(String socialId, Social provider);

    Mono<User> findBySocialIdAndProvider(String socialId, Social provider);
}
