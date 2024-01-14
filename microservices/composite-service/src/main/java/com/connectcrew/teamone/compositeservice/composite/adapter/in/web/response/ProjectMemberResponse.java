package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectMember;

import java.util.List;

public record ProjectMemberResponse(
        ProfileResponse profile,
        Boolean isLeader,
        List<String> parts
) {
    public ProjectMemberResponse(ProjectMember member, ProfileResponse profile) {
        this(
                profile,
                member.isLeader(),
                member.parts().stream().map(MemberPart::getDescription).toList()
        );
    }
}
