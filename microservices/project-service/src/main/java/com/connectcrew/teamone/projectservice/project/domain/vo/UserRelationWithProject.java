package com.connectcrew.teamone.projectservice.project.domain.vo;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

import java.util.Collection;

public record UserRelationWithProject(

        Long projectId,
        Long userId,
        Collection<MemberPart> members,
        Collection<MemberPart> applies
) {
}
