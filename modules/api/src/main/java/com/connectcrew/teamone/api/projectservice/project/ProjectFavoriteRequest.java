package com.connectcrew.teamone.api.projectservice.project;

public record ProjectFavoriteRequest(
        Long project,
        Boolean favorite
) {
}
