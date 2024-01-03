package com.connectcrew.teamone.projectservice.member.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.member.ApplyRequest;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.ApplyState;

public record ApplyCommand(
        Long userId,
        Long projectId,
        MemberPart part,
        String message
) {

    public static ApplyCommand from(ApplyRequest request) {
        return new ApplyCommand(
                request.userId(),
                request.projectId(),
                request.part(),
                request.message()
        );
    }

    public Apply toDomain(Long partId) {
        return Apply.builder()
                .userId(userId)
                .projectId(projectId)
                .partId(partId)
                .part(part)
                .message(message)
                .state(ApplyState.WAITING)
                .build();
    }
}
