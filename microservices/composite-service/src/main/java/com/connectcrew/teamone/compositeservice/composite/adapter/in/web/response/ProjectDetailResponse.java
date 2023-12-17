package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.ProjectDetail;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectCategory;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailResponse(
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
        ProfileResponse leader,
        String introduction,
        Integer favorite,
        Boolean myFavorite,
        String chatRoomId,
        List<RecruitStatusResponse> recruitStatus,
        List<String> skills
) {

    public ProjectDetailResponse(ProjectDetail detail, List<String> banners, Boolean myFavorite, ProfileResponse loader) {
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
                detail.chatRoomId(),
                detail.recruitStatuses().stream().map(RecruitStatusResponse::new).toList(),
                detail.skills()
        );
    }
}
