package com.connectcrew.teamone.projectservice.member.application.port.in;

import com.connectcrew.teamone.projectservice.member.domain.Member;
import reactor.core.publisher.Mono;

import java.util.List;

public interface QueryMemberUseCase {
    Mono<List<Member>> findAllByProject(Long project);
}
