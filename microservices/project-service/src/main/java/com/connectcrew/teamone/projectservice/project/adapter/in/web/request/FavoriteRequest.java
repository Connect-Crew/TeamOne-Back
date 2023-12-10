package com.connectcrew.teamone.projectservice.project.adapter.in.web.request;

import com.connectcrew.teamone.projectservice.project.application.port.in.command.FavoriteCommand;

public record FavoriteRequest(
        Long project,
        Boolean favorite
) {
    public FavoriteCommand toCommand() {
        return new FavoriteCommand(
                project,
                favorite
        );
    }
}
