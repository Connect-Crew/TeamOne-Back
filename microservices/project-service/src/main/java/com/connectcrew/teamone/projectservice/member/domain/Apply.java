package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyApiResponse;
import com.connectcrew.teamone.projectservice.member.domain.enums.ApplyState;
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
    public ApplyApiResponse toResponse() {
        return new ApplyApiResponse(id, userId, projectId, part, message);
    }

    public Apply accept() {
        return Apply.builder()
                .id(id)
                .userId(userId)
                .projectId(projectId)
                .partId(partId)
                .part(part)
                .message(message)
                .state(ApplyState.ACCEPT)
                .build();
    }

    public Apply reject() {
        return Apply.builder()
                .id(id)
                .userId(userId)
                .projectId(projectId)
                .partId(partId)
                .part(part)
                .message(message)
                .state(ApplyState.REJECT)
                .build();
    }
}
