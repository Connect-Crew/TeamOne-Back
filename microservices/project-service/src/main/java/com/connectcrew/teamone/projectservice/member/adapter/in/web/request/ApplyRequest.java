package com.connectcrew.teamone.projectservice.member.adapter.in.web.request;

import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.ApplyCommand;

public record ApplyRequest(
        Long userId,
        Long projectId,
        MemberPart part,
        String message
) {
    public ApplyCommand toCommand() {
        return new ApplyCommand(
                userId,
                projectId,
                part,
                message
        );
    }
}
