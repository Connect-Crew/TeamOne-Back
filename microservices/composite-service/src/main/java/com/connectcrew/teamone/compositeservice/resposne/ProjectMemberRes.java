package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.ProjectMember;
import com.connectcrew.teamone.api.project.values.MemberPart;

import java.util.List;

public record ProjectMemberRes(
        ProfileRes profile,
        List<String> parts
) {
    public ProjectMemberRes(ProjectMember member, ProfileRes profile) {
        this(
                profile,
                member.parts().stream().map(MemberPart::getDescription).toList()
        );
    }
}
