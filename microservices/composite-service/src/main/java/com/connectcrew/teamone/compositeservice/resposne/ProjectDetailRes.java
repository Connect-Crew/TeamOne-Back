package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.ProjectDetail;
import com.connectcrew.teamone.api.project.values.ProjectCategory;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailRes(
        Long id,
        String title,
        List<String> banners,
        String region,
        Boolean online,
        LocalDateTime createdAt,
        String state,
        String careerMin,
        String careerMax,
        List<String> category,
        String goal,
        ProfileRes leader,
        String introduction,
        Integer favorite,
        Boolean myFavorite,
        List<RecruitStatusRes> recruitStatus,
        List<String> skills
) {

    public ProjectDetailRes(ProjectDetail detail, List<String> banners, Boolean myFavorite, ProfileRes loader) {
        this(
                detail.id(),
                detail.title(),
                banners,
                detail.region().getDescription(),
                detail.online(),
                detail.createdAt(),
                detail.state().getDescription(),
                detail.careerMin().getDescription(),
                detail.careerMax().getDescription(),
                detail.category().stream().map(ProjectCategory::getDescription).toList(),
                detail.goal().getDescription(),
                loader,
                detail.introduction(),
                detail.favorite(),
                myFavorite,
                detail.recruitStatuses().stream().map(RecruitStatusRes::new).toList(),
                detail.skills()
        );
    }
}
