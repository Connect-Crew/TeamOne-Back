package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.ProjectDetail;
import com.connectcrew.teamone.api.project.values.ProjectCategory;
import com.connectcrew.teamone.api.user.profile.Profile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
        Profile leader,
        String introduction,
        Integer favorite,
        Boolean myFavorite,
        List<RecruitStatusRes> recruitStatus,
        List<ProjectMemberRes> members,
        List<String> skills
) {

    public ProjectDetailRes(ProjectDetail detail, Boolean myFavorite, Map<Long, Profile> profileMap) {
        this(
                detail.id(),
                detail.title(),
                detail.banners(),
                detail.region().getDescription(),
                detail.online(),
                detail.createdAt(),
                detail.state().getDescription(),
                detail.careerMin().getDescription(),
                detail.careerMax().getDescription(),
                detail.category().stream().map(ProjectCategory::getDescription).toList(),
                detail.goal().getDescription(),
                profileMap.get(detail.leader()),
                detail.introduction(),
                detail.favorite(),
                myFavorite,
                detail.recruitStatuses().stream().map(RecruitStatusRes::new).toList(),
                detail.members().stream().map(m -> new ProjectMemberRes(m, profileMap.get(m.memberId()))).toList(),
                detail.skills()
        );
    }
}
