package com.connectcrew.teamone.projectservice.member.application.port.in.command;

import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.projectservice.member.domain.Apply;

public record ApplyCommand(
        Long userId,
        Long projectId,
        MemberPart part,
        String message
) {

    public Apply toDomain(Long partId) {
        return Apply.builder()
                .userId(userId)
                .projectId(projectId)
                .part(partId)
                .message(message)
                .build();
    }
}
