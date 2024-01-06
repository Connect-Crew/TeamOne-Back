package com.connectcrew.teamone.projectservice.member.application.port.out;

import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import reactor.core.publisher.Mono;

public interface SaveMemberOutput {
    Mono<Member> save(Member member);

//    Mono<Long> saveMember(Long userId, List<Long> parts);

    Mono<Apply> saveApply(Apply apply);
}
