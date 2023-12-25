package com.connectcrew.teamone.projectservice.project.application.port.out;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectOption;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FindProjectOutput {
    Flux<ProjectItem> findAllByQuery(ProjectOption option);

    Mono<Project> findById(Long id);

    Mono<Long> findLeaderById(Long projectId);

    Mono<String> findProjectThumbnail(Long id);

    Mono<Boolean> existsProjectById(Long id);

    Mono<RecruitStatus> findByProjectAndPart(Long project, MemberPart part);

}
