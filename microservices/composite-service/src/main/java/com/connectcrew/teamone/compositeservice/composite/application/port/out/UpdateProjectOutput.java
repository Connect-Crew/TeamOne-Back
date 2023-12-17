package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.compositeservice.composite.domain.ProjectFavorite;
import reactor.core.publisher.Mono;

public interface UpdateProjectOutput {
    Mono<Integer> updateFavorite(ProjectFavorite favorite);

}
