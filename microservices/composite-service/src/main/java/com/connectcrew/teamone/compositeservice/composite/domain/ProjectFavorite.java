package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.projectservice.project.ProjectFavoriteApiRequest;

public record ProjectFavorite(
        Long project,
        Boolean favorite
) {
    public ProjectFavoriteApiRequest toApiRequest() {
        return new ProjectFavoriteApiRequest(
                project,
                favorite
        );
    }
}
