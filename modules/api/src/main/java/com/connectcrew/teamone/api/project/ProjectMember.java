package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.MemberPart;

import java.util.List;

public record ProjectMember(
        Long memberId,
        List<MemberPart> parts
) {
}
