package com.connectcrew.teamone.projectservice.project.application.port.out;

import com.connectcrew.teamone.api.projectservice.enums.Part;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.ProjectPart;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectOption;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FindProjectOutput {
    Flux<ProjectItem> findAllByQuery(ProjectOption option);

    Flux<ProjectItem> findAllByUserId(Long userId);

    Mono<Project> findById(Long id);

    Mono<Long> findLeaderById(Long projectId);

    Mono<String> findTitleById(Long projectId);

    Mono<String> findThumbnail(Long projectId);

    Mono<Boolean> existsById(Long projectId);
    Mono<Boolean> existsReportByProjectAndUser(Long project, Long user);
    Flux<ProjectPart> findAllProjectPartByProject(Long project);
    Mono<ProjectPart> findProjectPartByProjectAndPart(Long project, Part part);

}
