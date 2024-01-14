package com.connectcrew.teamone.compositeservice.composite.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.project.CreateRecruitApiRequest;

public record CreateRecruitCommand(
        MemberPart part,
        String comment,
        Long max
) {
    public static CreateRecruitApiRequest toApiRequest(CreateRecruitCommand command) {
        return new CreateRecruitApiRequest(
                command.part(),
                command.comment(),
                command.max()
        );
    }
}
