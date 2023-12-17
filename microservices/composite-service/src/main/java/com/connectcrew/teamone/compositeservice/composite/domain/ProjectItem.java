package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.Career;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectCategory;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectGoal;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectState;
import com.connectcrew.teamone.compositeservice.global.enums.Region;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectItem(
        Long id,
        String title,
        String thumbnail,
        Region region,
        Boolean online,
        Career careerMin,
        Career careerMax,
        LocalDateTime createdAt,
        ProjectState state,
        Integer favorite,
        List<ProjectCategory> category,
        ProjectGoal goal,
        List<RecruitStatus> recruitStatus
) {
}
