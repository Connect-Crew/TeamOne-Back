package com.connectcrew.teamone.api.projectservice.project;

public record ProjectFavoriteApiRequest(
        Long project,
        Boolean favorite
) {
}
