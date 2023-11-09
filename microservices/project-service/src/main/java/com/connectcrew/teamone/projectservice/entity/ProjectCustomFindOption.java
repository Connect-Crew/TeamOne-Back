package com.connectcrew.teamone.projectservice.entity;

import com.connectcrew.teamone.api.project.values.*;

import java.util.List;

public record ProjectCustomFindOption(
        Integer lastId,
        int size,
        ProjectGoal goal,
        Career career,
        List<Region> region,
        Boolean online,
        MemberPart part,
        List<SkillType> skills,
        List<ProjectState> states,
        List<ProjectCategory> category
) {
}
