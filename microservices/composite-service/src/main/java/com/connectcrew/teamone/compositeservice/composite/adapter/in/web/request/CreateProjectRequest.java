package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;


import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateProjectCommand;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record CreateProjectRequest(
        String title,
        Region region,
        Boolean online,
        ProjectState state,
        Career careerMin,
        Career careerMax,
        List<MemberPart> leaderParts,
        List<ProjectCategory> category,
        ProjectGoal goal,
        String introduction,
        List<CreateRecruitRequest> recruits,
        List<String> skills
) {
    public CreateProjectCommand toCommand(Long leader, UUID chatRoomId, List<String> banners) {
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
                recruits.stream().map(CreateRecruitRequest::toCommand).toList(),
                skills
        );
    }
}
