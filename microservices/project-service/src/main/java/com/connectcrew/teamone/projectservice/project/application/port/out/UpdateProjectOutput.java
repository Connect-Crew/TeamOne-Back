package com.connectcrew.teamone.projectservice.project.application.port.out;

import com.connectcrew.teamone.projectservice.project.domain.Project;
import reactor.core.publisher.Mono;

public interface UpdateProjectOutput {
    Mono<Integer> updateFavorite(Long project, Integer change);

    Mono<Project> update(Project project);
}
