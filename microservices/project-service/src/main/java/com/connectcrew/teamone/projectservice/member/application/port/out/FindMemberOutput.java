package com.connectcrew.teamone.projectservice.member.application.port.out;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FindMemberOutput {

    Mono<List<Member>> findAllByProject(Long project);
    Flux<MemberPart> findAllUserPartByProjectAndUser(Long project, Long user);

    Mono<Boolean> existsMemberByPartAndUser(Long partId, Long user);

    Mono<Boolean> existsApplyByPartAndUser(Long partId, Long user);

    Flux<Apply> findAllAppliesByProject(Long projectId);
    Flux<Apply> findAllApplies(Long projectId, MemberPart part);

    Mono<Apply> findApplyById(Long applyId);

    Mono<Member> findByUserId(Long userId);
}
