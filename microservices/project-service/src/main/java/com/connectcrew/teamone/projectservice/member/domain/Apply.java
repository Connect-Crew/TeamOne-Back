package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyApiResponse;
import com.connectcrew.teamone.api.projectservice.enums.ApplyState;
import lombok.Builder;

@Builder
public record Apply(
        Long id,
        Long userId,
        Long projectId,
        Long partId,
        MemberPart part,
        String message,
        ApplyState state,
        String leaderMessage
) {
    public ApplyApiResponse toResponse() {
        return new ApplyApiResponse(id, userId, projectId, part, message, state, leaderMessage);
    }

    public Apply accept(String leaderMessage) {
        return Apply.builder()
                .id(id)
                .userId(userId)
                .projectId(projectId)
                .partId(partId)
                .part(part)
                .message(message)
                .state(ApplyState.ACCEPT)
                .leaderMessage(leaderMessage)
                .build();
    }

    public Apply reject(String leaderMessage) {
        return Apply.builder()
                .id(id)
                .userId(userId)
                .projectId(projectId)
                .partId(partId)
                .part(part)
                .message(message)
                .state(ApplyState.REJECT)
                .leaderMessage(leaderMessage)
                .build();
    }
}
