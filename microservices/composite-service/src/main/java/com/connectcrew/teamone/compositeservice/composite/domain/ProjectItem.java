package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.api.projectservice.project.ProjectItemApiResponse;

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
        List<MemberPart> leaderParts,
        List<RecruitStatus> recruitStatus
) {

    public static ProjectItem of(ProjectItemApiResponse res) {
        return new ProjectItem(
                res.id(),
                res.title(),
                res.thumbnail(),
                res.region(),
                res.online(),
                res.careerMin(),
                res.careerMax(),
                res.createdAt(),
                res.state(),
                res.favorite(),
                res.category(),
                res.goal(),
                res.leaderParts(),
                res.recruitStatus().stream().map(RecruitStatus::of).toList()
        );
    }
}
