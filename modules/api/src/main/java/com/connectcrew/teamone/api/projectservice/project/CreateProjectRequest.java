package com.connectcrew.teamone.api.projectservice.project;


import com.connectcrew.teamone.api.projectservice.enums.*;

import java.util.List;
import java.util.UUID;

public record CreateProjectRequest(
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
        List<CreateRecruitRequest> recruits,
        List<String> skills
) {
}
