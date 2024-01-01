package com.connectcrew.teamone.compositeservice.composite.application.port.in.command;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.*;
import com.connectcrew.teamone.compositeservice.composite.domain.vo.ModifyProjectInfo;
import com.connectcrew.teamone.compositeservice.global.enums.Region;

import java.util.List;

public record ModifyProjectCommand(
        Long projectId,
        Long userId,
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        ProjectState state,
        Career careerMin,
        Career careerMax,
        List<MemberPart> leaderParts,
        List<ProjectCategory> category,
        ProjectGoal goal,
        String introduction,
        List<CreateRecruitCommand> recruits,
        List<String> skills
) {
    public ModifyProjectInfo toDomain() {
        return new ModifyProjectInfo(
                projectId,
                userId,
                title,
                banners,
                region,
                online,
                state,
                careerMin,
                careerMax,
                leaderParts,
                category,
                goal,
                introduction,
                recruits.stream().map(CreateRecruitCommand::toDomain).toList(),
                skills
        );
    }
}
