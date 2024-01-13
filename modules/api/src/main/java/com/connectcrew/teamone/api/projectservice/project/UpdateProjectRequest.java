package com.connectcrew.teamone.api.projectservice.project;


import com.connectcrew.teamone.api.projectservice.enums.*;

import java.util.List;

public record UpdateProjectRequest(
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
        List<CreateRecruitRequest> recruits,
        List<String> skills
) {
}
