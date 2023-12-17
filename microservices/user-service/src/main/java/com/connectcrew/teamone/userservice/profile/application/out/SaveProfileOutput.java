package com.connectcrew.teamone.userservice.profile.application.out;

import com.connectcrew.teamone.userservice.profile.domain.Profile;
import reactor.core.publisher.Mono;

public interface SaveProfileOutput {
    Mono<Profile> save(Profile profile);
}
