package com.connectcrew.teamone.projectservice.project.application.port.in;

import com.connectcrew.teamone.projectservice.project.application.port.in.command.FavoriteCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.UpdateProjectCommand;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import reactor.core.publisher.Mono;

public interface UpdateProjectUseCase {
    Mono<Integer> updateFavorite(FavoriteCommand command);

    Mono<Project> updateProject(UpdateProjectCommand command);
}
