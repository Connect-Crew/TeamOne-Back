package com.connectcrew.teamone.projectservice.member.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.member.domain.ProjectMemberPart;
import com.connectcrew.teamone.projectservice.member.domain.enums.MemberState;

import java.util.List;
import java.util.Map;

public record SaveMemberCommand(
        Long userId,
        Long projectId,
        List<MemberPart> parts
) {
    public Member toDomain(Map<MemberPart, Long> partIdMap) {
        return new Member(
                null,
                userId,
                projectId,
                parts.stream().map(p -> new ProjectMemberPart(null, partIdMap.get(p), userId, p)).toList(),
                MemberState.ACTIVE
        );
    }
}
