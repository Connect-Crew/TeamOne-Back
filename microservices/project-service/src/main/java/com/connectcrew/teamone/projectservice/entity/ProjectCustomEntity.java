package com.connectcrew.teamone.projectservice.entity;

import com.connectcrew.teamone.api.project.values.*;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectCustomEntity(
        Long id,
        String title,
        Region region,
        Boolean online,
        Career careerMin,
        Career careerMax,
        LocalDateTime createdAt,
        ProjectState state,
        Integer favorite,
        List<ProjectCategory> category,
        ProjectGoal goal
) {

}
