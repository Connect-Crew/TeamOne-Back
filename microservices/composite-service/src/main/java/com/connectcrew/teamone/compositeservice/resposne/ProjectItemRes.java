package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.ProjectItem;
import com.connectcrew.teamone.api.project.values.ProjectCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectItemRes(
        Long id,
        String title,
        String thumbnail,
        String region,
        Boolean online,
        String careerMin,
        String careerMax,
        LocalDateTime createdAt,
        LocalDate startDate,
        LocalDate endDate,
        String state,
        Integer favorite,
        List<String> category,
        String goal,
        List<RecruitStatusRes> recruitStatus
) {
    public ProjectItemRes(ProjectItem item) {
        this(
                item.id(),
                item.title(),
                item.thumbnail(),
                item.region().getDescription(),
                item.online(),
                item.careerMin().getDescription(),
                item.careerMax().getDescription(),
                item.createdAt(),
                item.startDate(),
                item.endDate(),
                item.state().getDescription(),
                item.favorite(),
                item.category().stream().map(ProjectCategory::name).toList(),
                item.goal().getDescription(),
                item.recruitStatus().stream().map(RecruitStatusRes::new).toList()
        );
    }
}
