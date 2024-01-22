package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.member.MemberApiResponse;
import com.connectcrew.teamone.projectservice.member.domain.enums.MemberState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Member(
        Long id,
        Long user,
        Long project,
        List<ProjectMemberPart> parts,
        MemberState state
) {
    public Member(Long user, Long project) {
        this(null, user, project, new ArrayList<>(), MemberState.ACTIVE);
    }

    public MemberApiResponse toResponse(Boolean isLeader) {
        return new MemberApiResponse(user, isLeader, parts.stream().map(ProjectMemberPart::part).toList());
    }

    public boolean containPart(MemberPart part) {
        return parts.stream().anyMatch(p -> p.part().equals(part));
    }

    public Member update(Map<MemberPart, Long> partIdMap, List<MemberPart> newParts) {
        Map<MemberPart, ProjectMemberPart> partMap = new HashMap<>();
        for (ProjectMemberPart part : this.parts) {
            partMap.put(part.part(), part);
        }

        return new Member(
                id,
                user,
                project,
                newParts.stream().map(p -> partMap.getOrDefault(p, new ProjectMemberPart(null, partIdMap.get(p), id, p))).toList(),
                state
        );
    }

    public Member addPart(MemberPart part, Long partId) {
        List<ProjectMemberPart> newParts = new ArrayList<>(parts);
        newParts.add(new ProjectMemberPart(null, partId, id, part));
        return new Member(id, user, project, newParts, state);
    }

    public Member kick() {
        return new Member(id, user, project, parts, MemberState.KICKED);
    }
}
