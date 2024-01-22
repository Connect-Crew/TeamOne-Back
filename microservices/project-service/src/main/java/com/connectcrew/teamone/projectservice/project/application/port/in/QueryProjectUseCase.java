package com.connectcrew.teamone.projectservice.project.application.port.in;

import com.connectcrew.teamone.projectservice.project.application.port.in.query.ProjectQuery;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QueryProjectUseCase {

    Flux<ProjectItem> findAllByQuery(ProjectQuery query);

    Flux<ProjectItem> findAllByUserId(Long userId);

    Mono<String> findProjectThumbnail(Long id);

    Mono<Long> findLeaderByProject(Long projectId);

    Mono<Project> findById(Long projectId);
}
