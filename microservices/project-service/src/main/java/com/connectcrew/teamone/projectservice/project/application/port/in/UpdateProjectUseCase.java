package com.connectcrew.teamone.projectservice.project.application.port.in;

import com.connectcrew.teamone.projectservice.project.application.port.in.command.FavoriteCommand;
import reactor.core.publisher.Mono;

public interface UpdateProjectUseCase {
    Mono<Integer> update(FavoriteCommand command);
}
