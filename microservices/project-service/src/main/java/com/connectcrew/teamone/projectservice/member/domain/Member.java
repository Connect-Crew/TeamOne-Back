package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.member.MemberApiResponse;
import com.connectcrew.teamone.projectservice.member.domain.enums.MemberState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Member(
        Long id,
        Long user,
        Long project,
        List<com.connectcrew.teamone.projectservice.member.domain.MemberPart> parts,
        MemberState state
) {
    public MemberApiResponse toResponse(Boolean isLeader) {
        return new MemberApiResponse(user, isLeader, parts.stream().map(com.connectcrew.teamone.projectservice.member.domain.MemberPart::part).toList());
    }

    public boolean containPart(MemberPart part) {
        return parts.stream().anyMatch(p -> p.part().equals(part));
    }

    public Member update(Map<MemberPart, Long> partIdMap, List<MemberPart> newParts) {
        Map<MemberPart, com.connectcrew.teamone.projectservice.member.domain.MemberPart> partMap = new HashMap<>();
        for (com.connectcrew.teamone.projectservice.member.domain.MemberPart part : this.parts) {
            partMap.put(part.part(), part);
        }

        return new Member(
                id,
                user,
                project,
                newParts.stream().map(p -> partMap.getOrDefault(p, new com.connectcrew.teamone.projectservice.member.domain.MemberPart(null, partIdMap.get(p), id, p))).toList(),
                state
        );
    }
}
