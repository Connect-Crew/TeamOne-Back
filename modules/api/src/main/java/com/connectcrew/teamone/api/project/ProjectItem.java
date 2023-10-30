package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.Career;
import com.connectcrew.teamone.api.project.values.ProjectGoal;
import com.connectcrew.teamone.api.project.values.ProjectState;
import com.connectcrew.teamone.api.project.values.Region;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectItem(
        String id,
        String title,
        String thumbnail,
        Region region,
        Boolean online,
        Career careerMin,
        Career careerMax,
        LocalDateTime createdAt,
        LocalDate startDate,
        LocalDate endDate,
        ProjectState state,
        Integer favorite,
        List<String> category,
        ProjectGoal goal,
        List<RecruitStatus> recruitStatus
) {
}
