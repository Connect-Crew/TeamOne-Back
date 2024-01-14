package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.enums.ProjectCategory;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectDetail;
import com.connectcrew.teamone.compositeservice.composite.domain.RecruitStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        UUID chatRoomId,
        List<RecruitStatusResponse> recruitStatus,
        List<String> skills
) {

    public ProjectDetailResponse(ProjectDetail detail, List<String> banners, Boolean myFavorite, ProfileResponse leader) {
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
                leader,
                detail.introduction(),
                detail.favorite(),
                myFavorite,
                detail.chatRoomId(),
                detail.recruitStatuses().stream().map(recruit -> new RecruitStatusResponse(recruit, containLeader(detail.leaderParts(), recruit))).toList(),
                detail.skills()
        );
    }

    private static boolean containLeader(List<MemberPart> leaderParts, RecruitStatus status) {
        return leaderParts.contains(status.part());
    }

}
