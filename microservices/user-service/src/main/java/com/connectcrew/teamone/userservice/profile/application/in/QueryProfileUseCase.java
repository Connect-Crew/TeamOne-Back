package com.connectcrew.teamone.userservice.profile.application.in;

import com.connectcrew.teamone.userservice.profile.domain.vo.FullProfile;
import reactor.core.publisher.Mono;

public interface QueryProfileUseCase {
    Mono<FullProfile> findProfileByUserId(Long userId);

    Mono<String> findUserNameByUserId(Long userId);
}
