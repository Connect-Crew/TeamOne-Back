package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.ProjectItem;
import com.connectcrew.teamone.api.project.values.ProjectCategory;
import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;

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
    public static ProjectItemRes from(ProjectItem item, Boolean myFavorite) {
        return new ProjectItemRes(
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
                item.recruitStatus().stream().map(RecruitStatusRes::new).toList()
        );
    }
}
