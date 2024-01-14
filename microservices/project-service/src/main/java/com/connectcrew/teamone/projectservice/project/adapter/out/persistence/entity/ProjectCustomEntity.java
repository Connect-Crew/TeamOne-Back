package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity;

import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.projectservice.project.domain.ProjectPart;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectCustomEntity(
        Long id,
        String title,
        Region region,
        Boolean online,
        Career careerMin,
        Career careerMax,
        LocalDateTime createdAt,
        ProjectState state,
        Integer favorite,
        List<ProjectCategory> category,
        ProjectGoal goal
) {
    public ProjectItem toItem(BannerEntity thumbnail, List<ProjectPart> recruitStatuses, List<MemberPart> leaderParts) {
        return ProjectItem.builder()
                .id(id)
                .title(title)
                .thumbnail(thumbnail.getPath())
                .region(region)
                .online(online)
                .careerMin(careerMin)
                .careerMax(careerMax)
                .createdAt(createdAt)
                .state(state)
                .favorite(favorite)
                .category(category)
                .goal(goal)
                .leaderParts(leaderParts)
                .recruitStatus(recruitStatuses)
                .build();
    }

}
