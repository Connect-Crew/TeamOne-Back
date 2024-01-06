package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.query.FindProjectListQuery;
import com.connectcrew.teamone.compositeservice.composite.domain.*;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface QueryProjectUseCase {

    Mono<List<ProjectItem>> getProjectList(FindProjectListQuery query);

    Mono<List<ProjectItem>> getProjectList(Long userId);

    Mono<ProjectDetail> find(Long id, Long userId);

    Flux<ProjectMember> getProjectMemberList(Long projectId);

    Flux<Apply> getApplies(Long userId, Long projectId, MemberPart part);

    Flux<ApplyStatus> getApplyStatus(Long userId, Long projectId);
}
