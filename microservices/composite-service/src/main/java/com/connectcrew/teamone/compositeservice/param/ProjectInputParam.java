package com.connectcrew.teamone.compositeservice.param;

import com.connectcrew.teamone.api.project.RecruitInput;
import com.connectcrew.teamone.api.project.values.*;

import java.time.LocalDate;
import java.util.List;

public record ProjectInputParam(
        String title,
        Region region,
        Boolean online,
        LocalDate start,
        LocalDate end,
        ProjectState state,
        Career careerMin,
        Career careerMax,
        List<MemberPart> leaderParts,
        List<ProjectCategory> category,
        ProjectGoal goal,
        String introduction,
        List<RecruitInput> recruits,
        List<SkillType> skills
) {
}
