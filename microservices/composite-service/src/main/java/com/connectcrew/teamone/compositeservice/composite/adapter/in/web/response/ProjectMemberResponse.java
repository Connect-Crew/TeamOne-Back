package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.ProjectMember;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;

import java.util.List;

public record ProjectMemberResponse(
        ProfileResponse profile,
        List<String> parts
) {
    public ProjectMemberResponse(ProjectMember member, ProfileResponse profile) {
        this(
                profile,
                member.parts().stream().map(MemberPart::getDescription).toList()
        );
    }
}
