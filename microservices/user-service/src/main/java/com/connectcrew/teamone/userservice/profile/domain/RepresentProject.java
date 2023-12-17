package com.connectcrew.teamone.userservice.profile.domain;

import lombok.Builder;

@Builder
public record RepresentProject(
        Long id,
        Long profileId,
        Long projectId
) {
}
