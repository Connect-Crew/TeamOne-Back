package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.ProjectMember;
import com.connectcrew.teamone.api.project.values.MemberPart;

import java.util.List;

public record ProjectMemberRes(
        String profile, // TODO 임시코드 (향후 객체로 수정),
        List<String> parts
) {
    public ProjectMemberRes(ProjectMember member) {
        this(
                "" + member.memberId(),
                member.parts().stream().map(MemberPart::getDescription).toList()
        );
    }
}
