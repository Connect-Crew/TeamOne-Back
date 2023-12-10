package com.connectcrew.teamone.projectservice.project.domain.vo;

import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
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
