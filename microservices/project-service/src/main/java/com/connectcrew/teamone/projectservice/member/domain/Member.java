package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.project.values.MemberPart;

import java.util.List;

public record Member(
        Long memberId,
        Boolean isLeader,
        List<MemberPart> parts
) {
    public Member setLeader(Boolean leader) {
        return new Member(memberId, leader, parts);
    }
}
