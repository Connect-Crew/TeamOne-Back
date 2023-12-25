package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.project.ProjectFavoriteRequest;

public record FavoriteCommand(
        Long project,
        Boolean favorite
) {

    public static FavoriteCommand from(ProjectFavoriteRequest request) {
        return new FavoriteCommand(
                request.project(),
                request.favorite()
        );
    }
}
