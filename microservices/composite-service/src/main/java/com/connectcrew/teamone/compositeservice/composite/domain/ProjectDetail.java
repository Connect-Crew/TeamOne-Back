package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.api.projectservice.project.ProjectApiResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record ProjectDetail(
        Long id,
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        LocalDateTime createdAt,
        ProjectState state,
        Career careerMin,
        Career careerMax,
        UUID chatRoomId,
        List<ProjectCategory> category,
        ProjectGoal goal,
        Long leader,
        List<MemberPart> leaderParts,
        String introduction,
        Integer favorite,
        List<RecruitStatus> recruitStatuses,
        List<String> skills
) {

    public static ProjectDetail of(ProjectApiResponse res) {
        return ProjectDetail.builder()
                .id(res.id())
                .title(res.title())
                .banners(res.banners())
                .region(res.region())
                .online(res.online())
                .createdAt(res.createdAt())
                .state(res.state())
                .careerMin(res.careerMin())
                .careerMax(res.careerMax())
                .chatRoomId(res.chatRoomId())
                .category(res.category())
                .goal(res.goal())
                .leader(res.leader())
                .leaderParts(res.leaderParts())
                .introduction(res.introduction())
                .favorite(res.favorite())
                .recruitStatuses(res.recruitStatuses().stream().map(RecruitStatus::of).toList())
                .skills(res.skills())
                .build();
    }

}
