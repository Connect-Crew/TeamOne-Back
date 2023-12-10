package com.connectcrew.teamone.projectservice.project.application.port.in;

import com.connectcrew.teamone.projectservice.project.application.port.in.query.ProjectQuery;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;
import com.connectcrew.teamone.projectservice.project.domain.vo.UserRelationWithProject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface QueryProjectUseCase {

    Flux<ProjectItem> findAllByQuery(ProjectQuery query);
    Mono<Tuple2<Project, UserRelationWithProject>> findById(Long id, Long userId);

    Mono<String> findProjectThumbnail(Long id);
}
