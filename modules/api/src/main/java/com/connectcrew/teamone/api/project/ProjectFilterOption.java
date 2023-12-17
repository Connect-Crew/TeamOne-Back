package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.*;

import java.util.List;

public record ProjectFilterOption(
        Integer lastId,
        int size,
        ProjectGoal goal,
        Career career,
        List<Region> region,
        Boolean online,
        MemberPart part,
        List<String> skills,
        List<ProjectState> states,
        List<ProjectCategory> category,
        String search
) {
}
