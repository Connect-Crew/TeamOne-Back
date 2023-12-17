package com.connectcrew.teamone.projectservice.project.adapter.in.web.request;

import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.CreateProjectCommand;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateProjectRequest(
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        ProjectState state,
        String chatRoomId,
        Career careerMin,
        Career careerMax,
        Long leader,
        List<MemberPart> leaderParts,
        List<ProjectCategory> category,
        ProjectGoal goal,
        String introduction,
        List<CreateRecruitRequest> recruits,
        List<String> skills
) {
    public CreateProjectCommand toCommand() {
        return new CreateProjectCommand(
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
                recruits,
                skills
        );
    }
}
