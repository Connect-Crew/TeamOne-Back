package com.connectcrew.teamone.projectservice.project.domain;

import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.api.projectservice.project.ProjectResponse;
import com.connectcrew.teamone.projectservice.project.domain.vo.UserRelationWithProject;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record Project(
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
        List<RecruitStatus> recruitStatuses,
        List<String> skills
) {
    public ProjectResponse toResponse(UserRelationWithProject user) {
        return ProjectResponse.builder()
                .id(id)
                .title(title)
                .banners(banners)
                .region(region)
                .online(online)
                .createdAt(createdAt)
                .state(state)
                .careerMin(careerMin)
                .careerMax(careerMax)
                .chatRoomId(chatRoomId)
                .category(category)
                .goal(goal)
                .leader(leader)
                .introduction(introduction)
                .favorite(favorite)
                .recruitStatuses(recruitStatuses.stream().map(r -> r.toResponse(user.applies().contains(r.part()))).toList())
                .skills(skills)
                .build();
    }

}
