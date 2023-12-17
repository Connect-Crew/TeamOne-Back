package com.connectcrew.teamone.projectservice.member.adapter.in.web.response;

import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.projectservice.member.domain.Member;

import java.util.List;

public record MemberResponse(
        Long memberId,
        Boolean isLeader,
        List<MemberPart> parts
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.memberId(),
                member.isLeader(),
                member.parts()
        );
    }

    public static List<MemberResponse> from(List<Member> members) {
        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
