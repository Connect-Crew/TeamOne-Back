package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.compositeservice.composite.domain.FullProfile;
import reactor.core.publisher.Mono;

public interface QueryProfileUseCase {
    Mono<FullProfile> getFullProfile(Long id);
}
