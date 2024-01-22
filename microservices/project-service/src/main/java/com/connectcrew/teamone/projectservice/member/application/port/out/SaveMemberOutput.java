package com.connectcrew.teamone.projectservice.member.application.port.out;

import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.Kick;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SaveMemberOutput {
    Mono<Member> save(Member member);

//    Mono<Long> saveMember(Long userId, List<Long> parts);

    Mono<Apply> saveApply(Apply apply);

    Mono<List<Apply>> saveAllApply(List<Apply> apply);

    Mono<Kick> saveKick(Kick kick);
}
