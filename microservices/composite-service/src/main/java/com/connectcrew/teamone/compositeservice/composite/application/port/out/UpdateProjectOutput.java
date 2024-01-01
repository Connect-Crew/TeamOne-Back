package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.compositeservice.composite.domain.ProjectFavorite;
import com.connectcrew.teamone.compositeservice.composite.domain.vo.ModifyProjectInfo;
import reactor.core.publisher.Mono;

public interface UpdateProjectOutput {
    Mono<Integer> updateFavorite(ProjectFavorite favorite);

    Mono<Long> update(ModifyProjectInfo project);
}
