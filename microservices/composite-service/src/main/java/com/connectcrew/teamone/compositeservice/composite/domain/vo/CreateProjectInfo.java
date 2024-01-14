package com.connectcrew.teamone.compositeservice.composite.domain.vo;


import com.connectcrew.teamone.api.projectservice.enums.*;

import java.util.List;

public record CreateProjectInfo(
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
        List<CreateRecruitInfo> recruits,
        List<String> skills
) {

}
