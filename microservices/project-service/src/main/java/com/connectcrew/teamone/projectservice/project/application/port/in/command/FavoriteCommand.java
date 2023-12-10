package com.connectcrew.teamone.projectservice.project.application.port.in.command;

public record FavoriteCommand(
        Long project,
        Boolean favorite
) {
}
