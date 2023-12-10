package com.connectcrew.teamone.projectservice.project.domain.vo;

import com.connectcrew.teamone.api.project.values.MemberPart;

import java.util.Set;

public record UserRelationWithProject(
        Set<MemberPart> applies
) {

    public static UserRelationWithProject from(Set<MemberPart> applies) {
        return new UserRelationWithProject(applies);
    }
}
