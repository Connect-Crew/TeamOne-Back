package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyApiResponse;
import com.connectcrew.teamone.api.projectservice.enums.ApplyState;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Apply(
        Long id,
        Long userId,
        Long projectId,
        Long partId,
        MemberPart part,
        String message,
        String contact,
        ApplyState state,
        String leaderMessage,
        LocalDateTime leaderResponseAt
) {
    public ApplyApiResponse toResponse() {
        return new ApplyApiResponse(id, userId, projectId, part, message, contact, state, leaderMessage, leaderResponseAt);
    }

    public Apply accept(String leaderMessage) {
        return Apply.builder()
                .id(id)
                .userId(userId)
                .projectId(projectId)
                .partId(partId)
                .part(part)
                .message(message)
                .contact(contact)
                .state(ApplyState.ACCEPT)
                .leaderMessage(leaderMessage)
                .leaderResponseAt(LocalDateTime.now())
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
                .contact(contact)
                .state(ApplyState.REJECT)
                .leaderMessage(leaderMessage)
                .leaderResponseAt(LocalDateTime.now())
                .build();
    }
}
