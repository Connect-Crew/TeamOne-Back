package com.connectcrew.teamone.projectservice.member.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.member.ApplyApiRequest;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.enums.ApplyState;

public record ApplyCommand(
        Long userId,
        Long projectId,
        MemberPart part,
        String message
) {

    public static ApplyCommand from(ApplyApiRequest request) {
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
