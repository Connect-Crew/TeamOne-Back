package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.project.values.MemberPart;

import java.util.List;

public record Member(
        Long memberId,
        List<MemberPart> parts
) {
}
