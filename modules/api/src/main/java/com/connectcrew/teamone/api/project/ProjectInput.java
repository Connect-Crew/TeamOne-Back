package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.*;

import java.time.LocalDate;
import java.util.List;

public record ProjectInput(
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        LocalDate start,
        LocalDate end,
        ProjectState state,
        Career careerMin,
        Career careerMax,
        Long leader,
        List<MemberPart> leaderParts,
        List<ProjectCategory> category,
        ProjectGoal goal,
        String introduction,
        List<RecruitInput> recruits,
        List<SkillType> skills
) {
}
