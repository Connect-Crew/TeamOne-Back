package com.connectcrew.teamone.compositeservice.composite.domain.vo;


import com.connectcrew.teamone.api.projectservice.enums.*;

import java.util.List;

public record ModifyProjectInfo(
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
        List<CreateRecruitInfo> recruits,
        List<String> skills
) {

}
