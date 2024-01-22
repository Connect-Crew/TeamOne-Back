package com.connectcrew.teamone.projectservice.project.domain;

import com.connectcrew.teamone.api.projectservice.enums.ProjectCategory;

public record Category(
        Long id,
        ProjectCategory category
) {
}
