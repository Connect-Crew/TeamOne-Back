package com.connectcrew.teamone.projectservice.member.application.port.out;

import com.connectcrew.teamone.projectservice.member.domain.Apply;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SaveMemberOutput {
    Mono<Long> saveMember(Long userId, List<Long> parts);

    Mono<Apply> saveApply(Apply apply);
}
