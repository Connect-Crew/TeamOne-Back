package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.profile.Profile;
import reactor.core.publisher.Mono;

public interface FindUserOutput {
    Mono<User> getUser(String socialId, Social provider);

    Mono<Profile> getProfile(Long id);
}
