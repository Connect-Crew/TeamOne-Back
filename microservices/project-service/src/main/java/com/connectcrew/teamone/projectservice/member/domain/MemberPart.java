package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.Part;

public record MemberPart(
        Long id,
        Long partId,
        Long memberId,
        Part part
) {
}
