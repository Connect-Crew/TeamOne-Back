package com.connectcrew.teamone.compositeservice.composite.adapter.out.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.ProjectItem;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.Career;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectCategory;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectGoal;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectState;
import com.connectcrew.teamone.compositeservice.global.enums.Region;

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
        List<RecruitStatusResponse> recruitStatus
) {

    public ProjectItem toDomain() {
        return new ProjectItem(
                id,
                title,
                thumbnail,
                region,
                online,
                careerMin,
                careerMax,
                createdAt,
                state,
                favorite,
                category,
                goal,
                recruitStatus.stream().map(RecruitStatusResponse::toDomain).toList()
        );
    }
}
