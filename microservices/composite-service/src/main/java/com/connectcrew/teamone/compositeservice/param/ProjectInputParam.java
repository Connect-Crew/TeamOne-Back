package com.connectcrew.teamone.compositeservice.param;

import com.connectcrew.teamone.api.project.ProjectInput;
import com.connectcrew.teamone.api.project.RecruitInput;
import com.connectcrew.teamone.api.project.values.*;

import java.util.List;
import java.util.UUID;

public record ProjectInputParam(
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
        List<RecruitInput> recruits,
        List<String> skills
) {
    public ProjectInput toCommand(Long leader, UUID chatRoomId) {
        return new ProjectInput(
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
                recruits,
                skills
        );
    }
}
