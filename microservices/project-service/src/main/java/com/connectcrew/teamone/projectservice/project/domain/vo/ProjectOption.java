package com.connectcrew.teamone.projectservice.project.domain.vo;

import com.connectcrew.teamone.api.projectservice.enums.*;

import java.util.List;

public record ProjectOption(
        Integer lastId,
        int size,
        ProjectGoal goal,
        Career career,
        List<Region> region,
        Boolean online,
        Part part,
        List<String> skills,
        List<ProjectState> states,
        List<ProjectCategory> category,
        String search
) {
}
