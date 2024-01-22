package com.connectcrew.teamone.projectservice.project.application.port.out;

import com.connectcrew.teamone.api.projectservice.enums.ProjectState;
import com.connectcrew.teamone.projectservice.project.domain.ProjectPart;
import reactor.core.publisher.Mono;

public interface UpdateProjectOutput {
    Mono<Integer> favorite(Long project, Integer change);

    Mono<ProjectPart> updateCollected(Long partId, Integer change);

    Mono<ProjectState> updateState(Long projectId, ProjectState projectState);
}
