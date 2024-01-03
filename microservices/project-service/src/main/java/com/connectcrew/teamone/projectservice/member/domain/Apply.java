package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyResponse;
import lombok.Builder;

@Builder
public record Apply(
        Long id,
        Long userId,
        Long projectId,
        Long partId,
        MemberPart part,
        String message,
        ApplyState state
) {
    public ApplyResponse toResponse() {
        return new ApplyResponse(id, userId, projectId, part, message);
    }
}
