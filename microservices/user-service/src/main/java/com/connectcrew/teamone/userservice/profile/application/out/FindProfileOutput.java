package com.connectcrew.teamone.userservice.profile.application.out;

import com.connectcrew.teamone.userservice.profile.domain.Part;
import com.connectcrew.teamone.userservice.profile.domain.Profile;
import com.connectcrew.teamone.userservice.profile.domain.RepresentProject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FindProfileOutput {
    Mono<Boolean> existsByNickname(String nickname);

    Mono<Profile> findByUserId(Long userId);
    Flux<Part> findAllPartByProfileId(Long profileId);
    Flux<RepresentProject> finAllRepresentProjectIdByProfileId(Long profileId);
}
