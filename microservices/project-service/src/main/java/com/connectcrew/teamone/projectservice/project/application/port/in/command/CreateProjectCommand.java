package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.projectservice.project.adapter.in.web.request.CreateRecruitRequest;
import com.connectcrew.teamone.projectservice.project.domain.Project;

import java.time.LocalDateTime;
import java.util.List;

public record CreateProjectCommand(
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        ProjectState state,
        String chatRoomId,
        Career careerMin,
        Career careerMax,
        Long leader,
        List<MemberPart> leaderParts,
        List<ProjectCategory> category,
        ProjectGoal goal,
        String introduction,
        List<CreateRecruitRequest> recruits,
        List<String> skills
) {
    public Project toDomain() {
        return Project.builder()
                .title(title)
                .banners(banners)
                .region(region)
                .online(online)
                .createdAt(LocalDateTime.now())
                .state(state)
                .careerMin(careerMin)
                .careerMax(careerMax)
                .chatRoomId(chatRoomId)
                .category(category)
                .goal(goal)
                .leader(leader)
                .introduction(introduction)
                .favorite(0)
                .recruitStatuses(recruits.stream().map(r -> r.toDomain(leaderParts.contains(r.part()))).toList())
                .skills(skills)
                .build();
    }
}
