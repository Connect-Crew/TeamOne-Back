package com.connectcrew.teamone.compositeservice.composite.adapter.out.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.ProjectMember;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;

import java.util.List;

public record MemberResponse(
        Long memberId,
        List<MemberPart> parts
) {
    public ProjectMember toDomain() {
        return new ProjectMember(
                memberId,
                parts
        );
    }
}
