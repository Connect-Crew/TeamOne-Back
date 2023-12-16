package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.ProjectMember;
import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.ProfileResponse;

import java.util.List;

public record ProjectMemberRes(
        ProfileResponse profile,
        List<String> parts
) {
    public ProjectMemberRes(ProjectMember member, ProfileResponse profile) {
        this(
                profile,
                member.parts().stream().map(MemberPart::getDescription).toList()
        );
    }
}
