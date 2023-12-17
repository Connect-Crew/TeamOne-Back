package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;

import java.util.List;

public record ProjectMember(
        Long memberId,
        List<MemberPart> parts
) {
}
