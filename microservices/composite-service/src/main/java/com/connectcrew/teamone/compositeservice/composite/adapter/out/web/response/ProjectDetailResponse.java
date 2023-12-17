package com.connectcrew.teamone.compositeservice.composite.adapter.out.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.ProjectDetail;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.Career;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectCategory;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectGoal;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectState;
import com.connectcrew.teamone.compositeservice.global.enums.Region;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailResponse(
        Long id,
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        LocalDateTime createdAt,
        ProjectState state,
        Career careerMin,
        Career careerMax,
        String chatRoomId,
        List<ProjectCategory> category,
        ProjectGoal goal,
        Long leader,
        String introduction,
        Integer favorite,
        List<RecruitStatusResponse> recruitStatuses,
        List<String> skills
) {
    public ProjectDetail toDomain() {
        return new ProjectDetail(
                id,
                title,
                banners,
                region,
                online,
                createdAt,
                state,
                careerMin,
                careerMax,
                chatRoomId,
                category,
                goal,
                leader,
                introduction,
                favorite,
                recruitStatuses.stream().map(RecruitStatusResponse::toDomain).toList(),
                skills
        );
    }
}
