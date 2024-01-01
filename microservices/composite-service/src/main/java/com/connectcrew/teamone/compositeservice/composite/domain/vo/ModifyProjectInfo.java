package com.connectcrew.teamone.compositeservice.composite.domain.vo;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.*;
import com.connectcrew.teamone.compositeservice.global.enums.Region;

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
