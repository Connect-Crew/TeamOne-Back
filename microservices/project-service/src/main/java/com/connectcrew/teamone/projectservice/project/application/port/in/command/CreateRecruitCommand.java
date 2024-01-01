package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.project.CreateRecruitRequest;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;

public record CreateRecruitCommand(
        MemberPart part,
        String comment,
        Integer max
) {

    public static CreateRecruitCommand from(CreateRecruitRequest request) {
        return new CreateRecruitCommand(
                request.part(),
                request.comment(),
                request.max()
        );
    }

    public RecruitStatus toDomain(RecruitStatus origin) {
        return new RecruitStatus(
                origin.id(),
                origin.part(),
                comment,
                origin.current(),
                max
        );
    }
}
