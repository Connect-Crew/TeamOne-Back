package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.Part;
import com.connectcrew.teamone.api.projectservice.project.CreateRecruitRequest;
import com.connectcrew.teamone.projectservice.project.domain.ProjectPart;

public record CreateRecruitCommand(
        Part part,
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

    public ProjectPart toDomain(boolean leaderParts) {
        return ProjectPart.builder()
                .part(part)
                .comment(comment)
                .current(leaderParts ? 1 : 0)
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
