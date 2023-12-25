package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.member.MemberResponse;

import java.util.List;

public record Member(
        Long memberId,
        Boolean isLeader,
        List<MemberPart> parts
) {
    public MemberResponse toResponse() {
        return new MemberResponse(memberId, isLeader, parts);
    }

    public Member setLeader(Boolean leader) {
        return new Member(memberId, leader, parts);
    }
}
