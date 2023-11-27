package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.ProjectItem;
import com.connectcrew.teamone.api.project.values.ProjectCategory;

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
        String state,
        Integer favorite,
        Boolean myFavorite,
        List<String> category,
        String goal,
        List<RecruitStatusRes> recruitStatus
) {
    public ProjectItemRes(ProjectItem item, Boolean myFavorite, String thumbnail) {
        this(
                item.id(),
                item.title(),
                thumbnail,
                item.region().getDescription(),
                item.online(),
                item.careerMin().getDescription(),
                item.careerMax().getDescription(),
                item.createdAt(),
                item.state().getDescription(),
                item.favorite(),
                myFavorite,
                item.category().stream().map(ProjectCategory::getDescription).toList(),
                item.goal().getDescription(),
                item.recruitStatus().stream().map(RecruitStatusRes::new).toList()
        );
    }
}
