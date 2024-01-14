package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.api.projectservice.project.ProjectFavoriteApiRequest;
import com.connectcrew.teamone.api.projectservice.project.UpdateProjectApiRequest;
import reactor.core.publisher.Mono;

public interface UpdateProjectOutput {
    Mono<Integer> updateFavorite(ProjectFavoriteApiRequest favorite);

    Mono<Long> update(UpdateProjectApiRequest project);
}
