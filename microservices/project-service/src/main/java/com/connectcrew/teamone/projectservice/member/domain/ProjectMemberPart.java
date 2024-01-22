package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

public record ProjectMemberPart(
        Long id,
        Long partId,
        Long memberId,
        MemberPart part
) {

    public ProjectMemberPart(Long partId, MemberPart part) {
        this(null, partId, null, part);
    }
}
