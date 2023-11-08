package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.api.user.profile.Profile;
import reactor.core.publisher.Mono;

public interface ProfileRequest {
    Mono<Profile> getProfile(Long id);
}
