package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.project.CreateRecruitApiRequest;
import com.connectcrew.teamone.projectservice.project.domain.ProjectPart;

public record CreateRecruitCommand(
        MemberPart part,
        String comment,
        Long max
) {

    public static CreateRecruitCommand from(CreateRecruitApiRequest request) {
        return new CreateRecruitCommand(
                request.part(),
                request.comment(),
                request.max()
        );
    }

    public ProjectPart toDomain(boolean leaderParts) {
        return ProjectPart.builder()
                .part(part)
                .comment(comment)
                .current(leaderParts ? 1L : 0L)
                .max(max)
                .build();
    }

    public ProjectPart toDomain(ProjectPart origin) {
        return new ProjectPart(
                origin.id(),
                origin.part(),
                comment,
                origin.current(),
                max
        );
    }
}
