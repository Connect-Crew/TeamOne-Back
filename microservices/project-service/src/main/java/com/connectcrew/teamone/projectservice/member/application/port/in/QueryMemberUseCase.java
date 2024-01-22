package com.connectcrew.teamone.projectservice.member.application.port.in;

import com.connectcrew.teamone.projectservice.member.application.port.in.query.ProjectApplyQuery;
import com.connectcrew.teamone.projectservice.member.application.port.in.query.ProjectApplyStatusQuery;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.ApplyStatus;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.project.domain.vo.UserRelationWithProject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QueryMemberUseCase {
    Mono<Member> findMemberByProjectAndUser(Long project, Long user);
    Flux<Member> findAllByProject(Long project);

    Mono<UserRelationWithProject> findUserRelationByProjectAndUser(Long projectId, Long userId);

    Flux<Apply> findAllApplies(ProjectApplyQuery query);

    Flux<ApplyStatus> findAllApplyStatus(ProjectApplyStatusQuery query);
}
