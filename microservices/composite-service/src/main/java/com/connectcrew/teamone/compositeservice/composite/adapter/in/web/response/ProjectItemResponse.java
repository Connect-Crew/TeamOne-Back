package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import com.connectcrew.teamone.api.projectservice.enums.ProjectCategory;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectItem;
import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectItemResponse(
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
        List<RecruitStatusResponse> recruitStatus
) {
    public static ProjectItemResponse from(ProjectItem item, Boolean myFavorite) {
        return new ProjectItemResponse(
                item.id(),
                item.title(),
                FileCategory.BANNER.getUrlPath(item.thumbnail()),
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
                item.recruitStatus().stream().map(recruit -> new RecruitStatusResponse(recruit, false)).toList()
        );
    }
}
