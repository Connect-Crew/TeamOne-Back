package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.api.projectservice.project.CreateProjectRequest;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;

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
        List<CreateRecruitCommand> recruits,
        List<String> skills
) {
    public static CreateProjectCommand from(CreateProjectRequest request) {
        return new CreateProjectCommand(
                request.title(),
                request.banners(),
                request.region(),
                request.online(),
                request.state(),
                request.chatRoomId(),
                request.careerMin(),
                request.careerMax(),
                request.leader(),
                request.leaderParts(),
                request.category(),
                request.goal(),
                request.introduction(),
                request.recruits().stream().map(CreateRecruitCommand::from).toList(),
                request.skills()
        );
    }

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
                .recruitStatuses(recruits.stream().map(r -> RecruitStatus.from(r, leaderParts.contains(r.part()))).toList())
                .skills(skills)
                .build();
    }
}
