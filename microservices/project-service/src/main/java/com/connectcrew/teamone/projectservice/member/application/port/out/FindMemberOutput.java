package com.connectcrew.teamone.projectservice.member.application.port.out;

import com.connectcrew.teamone.api.projectservice.enums.Part;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FindMemberOutput {

    Flux<Member> findAllByProject(Long project);

    Mono<Member> findByProjectAndUser(Long project, Long user);

    Flux<Apply> findAllByProjectAndUser(Long project, Long user);

    Mono<Boolean> existsMemberByPartAndUser(Long partId, Long user);

    Mono<Boolean> existsApplyByPartAndUser(Long partId, Long user);

    Flux<Apply> findAllApplyByProject(Long projectId);
    Flux<Apply> findAllApplyByProjectAndPart(Long projectId, Part part);

    Mono<Apply> findApplyById(Long applyId);
}
