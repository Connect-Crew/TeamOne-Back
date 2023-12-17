package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.compositeservice.composite.domain.Profile;
import com.connectcrew.teamone.compositeservice.composite.domain.User;
import com.connectcrew.teamone.compositeservice.global.enums.Social;
import reactor.core.publisher.Mono;

public interface FindUserOutput {
    Mono<User> getUser(String socialId, Social provider);

    Mono<Profile> getProfile(Long id);
}
