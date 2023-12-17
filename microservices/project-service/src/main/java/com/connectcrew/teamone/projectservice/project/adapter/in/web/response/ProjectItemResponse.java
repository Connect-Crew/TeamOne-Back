package com.connectcrew.teamone.projectservice.project.adapter.in.web.response;

import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectItemResponse(
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

    public static ProjectItemResponse from(ProjectItem projectItem) {
        return new ProjectItemResponse(
                projectItem.id(),
                projectItem.title(),
                projectItem.thumbnail(),
                projectItem.region(),
                projectItem.online(),
                projectItem.careerMin(),
                projectItem.careerMax(),
                projectItem.createdAt(),
                projectItem.state(),
                projectItem.favorite(),
                projectItem.category(),
                projectItem.goal(),
                projectItem.recruitStatus()
        );
    }
}
