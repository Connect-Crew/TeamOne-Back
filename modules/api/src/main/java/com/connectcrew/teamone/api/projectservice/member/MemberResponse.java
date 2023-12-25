package com.connectcrew.teamone.api.projectservice.member;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

import java.util.List;

public record MemberResponse(
        Long memberId,
        Boolean isLeader,
        List<MemberPart> parts
) {
}
