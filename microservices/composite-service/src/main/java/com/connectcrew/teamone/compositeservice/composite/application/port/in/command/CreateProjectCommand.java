package com.connectcrew.teamone.compositeservice.composite.application.port.in.command;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.*;
import com.connectcrew.teamone.compositeservice.composite.domain.vo.CreateProjectInfo;
import com.connectcrew.teamone.compositeservice.global.enums.Region;

import java.util.List;

public record  CreateProjectCommand(
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
        List<CreateRecruitCommand> recruits,
        List<String> skills
) {
    public CreateProjectInfo toDomain() {
        return new CreateProjectInfo(
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
                recruits.stream().map(CreateRecruitCommand::toDomain).toList(),
                skills
        );
    }
}
