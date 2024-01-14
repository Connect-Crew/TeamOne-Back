package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.project.ProjectFavoriteApiRequest;

public record FavoriteCommand(
        Long project,
        Boolean favorite
) {

    public static FavoriteCommand from(ProjectFavoriteApiRequest request) {
        return new FavoriteCommand(
                request.project(),
                request.favorite()
        );
    }
}
