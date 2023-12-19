package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;


import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateProjectCommand;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.*;
import com.connectcrew.teamone.compositeservice.global.enums.Region;
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
    public CreateProjectCommand toCommand(Long leader, UUID chatRoomId) {
        return new CreateProjectCommand(
                title,
                List.of(),
                region,
                online,
                state,
                chatRoomId.toString(),
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
