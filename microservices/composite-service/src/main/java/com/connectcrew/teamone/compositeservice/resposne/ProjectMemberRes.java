package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.ProjectMember;
import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.api.user.profile.Profile;

import java.util.List;

public record ProjectMemberRes(
        Profile profile,
        List<String> parts
) {
    public ProjectMemberRes(ProjectMember member, Profile profile) {
        this(
                profile,
                member.parts().stream().map(MemberPart::getDescription).toList()
        );
    }
}
