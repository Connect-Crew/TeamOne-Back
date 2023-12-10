package com.connectcrew.teamone.projectservice.project.adapter.in.web.response;

import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.vo.UserRelationWithProject;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProjectResponse(
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

    public static ProjectResponse from(Project project, UserRelationWithProject user) {
        return ProjectResponse.builder()
                .id(project.id())
                .title(project.title())
                .banners(project.banners())
                .region(project.region())
                .online(project.online())
                .createdAt(project.createdAt())
                .state(project.state())
                .careerMin(project.careerMin())
                .careerMax(project.careerMax())
                .chatRoomId(project.chatRoomId())
                .category(project.category())
                .goal(project.goal())
                .leader(project.leader())
                .introduction(project.introduction())
                .favorite(project.favorite())
                .recruitStatuses(RecruitStatusResponse.from(project.recruitStatuses(), user.applies()))
                .skills(project.skills())
                .build();
    }
}
