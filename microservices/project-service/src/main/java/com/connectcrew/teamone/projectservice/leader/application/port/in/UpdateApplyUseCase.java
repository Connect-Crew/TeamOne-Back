package com.connectcrew.teamone.projectservice.leader.application.port.in;

import com.connectcrew.teamone.projectservice.member.domain.Apply;
import reactor.core.publisher.Mono;

public interface UpdateApplyUseCase {
    Mono<Apply> accept(Long applyId, Long leaderId);

    Mono<Apply> reject(Long applyId, Long leaderId);
}
