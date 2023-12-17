package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.RepresentProject;

public record RepresentProjectResponse(
        Long id,
        String thumbnail
) {

    public static RepresentProjectResponse from(RepresentProject representProject) {
        return new RepresentProjectResponse(
                representProject.id(),
                representProject.thumbnail()
        );
    }
}
