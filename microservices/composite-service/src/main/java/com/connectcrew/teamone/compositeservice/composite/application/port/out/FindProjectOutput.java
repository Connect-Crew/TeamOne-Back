package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.project.ProjectFilterOptionApiRequest;
import com.connectcrew.teamone.compositeservice.composite.domain.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FindProjectOutput {
    Mono<String> findProjectThumbnail(Long id);

    Flux<ProjectItem> findAllProjectItems(ProjectFilterOptionApiRequest option);

    Flux<ProjectItem> findAllProjectItems(Long userId);

    Mono<ProjectDetail> find(Long projectId, Long userId);

    Flux<ProjectMember> findMembers(Long projectId);

    Flux<Apply> findAllApplies(Long userId, Long projectId, MemberPart part);

    Flux<ApplyStatus> findAllApplyStatus(Long userId, Long projectId);
}
