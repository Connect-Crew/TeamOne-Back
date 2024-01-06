package com.connectcrew.teamone.projectservice.project.domain.vo;

import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.api.projectservice.project.ProjectItemResponse;
import com.connectcrew.teamone.projectservice.project.domain.ProjectPart;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProjectItem(
        Long id,
        String title,
        String thumbnail,
        Region region,
        Boolean online,
        Career careerMin,
        Career careerMax,
        LocalDateTime createdAt,
        ProjectState state,
        Integer favorite,
        List<ProjectCategory> category,
        ProjectGoal goal,
        List<ProjectPart> recruitStatus
) {
    public ProjectItemResponse toResponse() {
        return ProjectItemResponse.builder()
                .id(id)
                .title(title)
                .thumbnail(thumbnail)
                .region(region)
                .online(online)
                .careerMin(careerMin)
                .careerMax(careerMax)
                .createdAt(createdAt)
                .state(state)
                .favorite(favorite)
                .category(category)
                .goal(goal)
                .recruitStatus(recruitStatus.stream().map(r -> r.toResponse(false)).toList())
                .build();
    }

//    public ProjectItemResponse toResponse(Collection<Part> applies) {
//        return ProjectItemResponse.builder()
//                .id(id)
//                .title(title)
//                .thumbnail(thumbnail)
//                .region(region)
//                .online(online)
//                .careerMin(careerMin)
//                .careerMax(careerMax)
//                .createdAt(createdAt)
//                .state(state)
//                .favorite(favorite)
//                .category(category)
//                .goal(goal)
//                .recruitStatus(recruitStatus.stream().map(r -> r.toResponse(applies.contains(r.part()))).toList())
//                .build();
//    }
}
