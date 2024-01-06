package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.Part;
import com.connectcrew.teamone.api.projectservice.member.MemberResponse;
import com.connectcrew.teamone.projectservice.member.domain.enums.MemberState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Member(
        Long id,
        Long user,
        Long project,
        List<MemberPart> parts,
        MemberState state
) {
    public MemberResponse toResponse(Boolean isLeader) {
        return new MemberResponse(user, isLeader, parts.stream().map(MemberPart::part).toList());
    }

    public boolean containPart(Part part) {
        return parts.stream().anyMatch(p -> p.part().equals(part));
    }

    public Member update(Map<Part, Long> partIdMap, List<Part> newParts) {
        Map<Part, MemberPart> partMap = new HashMap<>();
        for (MemberPart part : this.parts) {
            partMap.put(part.part(), part);
        }

        return new Member(
                id,
                user,
                project,
                newParts.stream().map(p -> partMap.getOrDefault(p, new MemberPart(null, partIdMap.get(p), id, p))).toList(),
                state
        );
    }
}
