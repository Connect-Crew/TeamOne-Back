package com.connectcrew.teamone.projectservice.member.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.Part;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.member.domain.MemberPart;
import com.connectcrew.teamone.projectservice.member.domain.enums.MemberState;

import java.util.List;
import java.util.Map;

public record SaveMemberCommand(
        Long userId,
        Long projectId,
        List<Part> parts
) {
    public Member toDomain(Map<Part, Long> partIdMap) {
        return new Member(
                null,
                userId,
                projectId,
                parts.stream().map(p -> new MemberPart(null, partIdMap.get(p), userId, p)).toList(),
                MemberState.ACTIVE
        );
    }
}
