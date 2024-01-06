package com.connectcrew.teamone.projectservice.project.domain.vo;

import com.connectcrew.teamone.api.projectservice.enums.Part;

import java.util.Collection;

public record UserRelationWithProject(

        Long projectId,
        Long userId,
        Collection<Part> members,
        Collection<Part> applies
) {
}
