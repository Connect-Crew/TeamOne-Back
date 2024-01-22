package com.connectcrew.teamone.compositeservice.composite.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.api.projectservice.project.CreateProjectApiRequest;

import java.util.List;
import java.util.UUID;

public record  CreateProjectCommand(
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        ProjectState state,
        UUID chatRoomId,
        Career careerMin,
        Career careerMax,
        Long leader,
        List<MemberPart> leaderParts,
        List<ProjectCategory> category,
        ProjectGoal goal,
        String introduction,
        List<CreateRecruitCommand> recruits,
        List<String> skills
) {

    public CreateProjectApiRequest toApiRequest() {
        return new CreateProjectApiRequest(
                title,
                banners,
                region,
                online,
                state,
                chatRoomId,
                careerMin,
                careerMax,
                leader,
                leaderParts,
                category,
                goal,
                introduction,
                recruits.stream().map(CreateRecruitCommand::toApiRequest).toList(),
                skills
        );
    }
}
