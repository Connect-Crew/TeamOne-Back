package com.connectcrew.teamone.projectservice.member.domain;

public record MemberPart(
        Long id,
        Long partId,
        Long memberId,
        com.connectcrew.teamone.api.projectservice.enums.MemberPart part
) {
}
