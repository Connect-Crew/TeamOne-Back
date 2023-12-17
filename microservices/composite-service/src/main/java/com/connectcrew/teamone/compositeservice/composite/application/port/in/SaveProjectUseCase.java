package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateProjectCommand;
import com.connectcrew.teamone.compositeservice.composite.domain.Apply;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectFavorite;
import com.connectcrew.teamone.compositeservice.composite.domain.Report;
import reactor.core.publisher.Mono;

public interface SaveProjectUseCase {

    Mono<Long> save(CreateProjectCommand command);

    Mono<Boolean> save(Apply apply);

    Mono<Boolean> save(Report report);

    Mono<Integer> setFavorite(ProjectFavorite favorite);
}
