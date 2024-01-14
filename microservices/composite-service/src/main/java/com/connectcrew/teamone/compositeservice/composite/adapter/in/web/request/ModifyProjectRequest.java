package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.ModifyProjectCommand;
import lombok.Builder;

import java.util.List;

@Builder
public record ModifyProjectRequest(
        Long projectId,
        Long userId,
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
    public ModifyProjectCommand toCommand(List<String> bannerPaths, List<String> removeBannerPaths) {
        return new ModifyProjectCommand(
                projectId,
                userId,
                title,
                bannerPaths,
                removeBannerPaths,
                region,
                online,
                state,
                careerMin,
                careerMax,
                leaderParts,
                category,
                goal,
                introduction,
                recruits.stream().map(CreateRecruitRequest::toCommand).toList(),
                skills
        );
    }
}
