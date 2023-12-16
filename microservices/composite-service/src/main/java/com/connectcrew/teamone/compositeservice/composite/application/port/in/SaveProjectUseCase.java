package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.api.project.ApplyInput;
import com.connectcrew.teamone.api.project.ProjectInput;
import com.connectcrew.teamone.api.project.ReportInput;
import com.connectcrew.teamone.compositeservice.param.ProjectFavoriteParam;
import reactor.core.publisher.Mono;

public interface SaveProjectUseCase {

    Mono<Long> save(ProjectInput input);

    Mono<Boolean> save(ApplyInput input);

    Mono<Boolean> save(ReportInput input);

    Mono<Integer> setFavorite(ProjectFavoriteParam param);
}
