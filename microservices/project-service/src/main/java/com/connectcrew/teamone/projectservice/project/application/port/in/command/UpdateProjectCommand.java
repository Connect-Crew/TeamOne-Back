package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.api.projectservice.project.UpdateProjectRequest;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record UpdateProjectCommand(
        Long projectId,
        Long userId,
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        ProjectState state,
        Career careerMin,
        Career careerMax,
        List<MemberPart> leaderParts,
        List<ProjectCategory> category,
        ProjectGoal goal,
        String introduction,
        List<CreateRecruitCommand> recruits,
        List<String> skills
) {

    public static UpdateProjectCommand from(UpdateProjectRequest request) {
        return new UpdateProjectCommand(
                request.projectId(),
                request.userId(),
                request.title(),
                request.banners(),
                request.region(),
                request.online(),
                request.state(),
                request.careerMin(),
                request.careerMax(),
                request.leaderParts(),
                request.category(),
                request.goal(),
                request.introduction(),
                request.recruits().stream().map(CreateRecruitCommand::from).toList(),
                request.skills()
        );
    }

    public Project toDomain(Project origin) {
        Map<MemberPart, Integer> partMemberCountMap = origin.recruitStatuses().stream()
                .collect(Collectors.toMap(RecruitStatus::part, RecruitStatus::current));

        List<RecruitStatus> newRecruits = recruits.stream()
                .map(r -> RecruitStatus.from(r, partMemberCountMap.getOrDefault(r.part(), 0)))
                .toList();

        return new Project(
                projectId,
                title,
                banners,
                region,
                online,
                origin.createdAt(),
                state,
                careerMin,
                careerMax,
                origin.chatRoomId(),
                category,
                goal,
                origin.leader(),
                introduction,
                origin.favorite(),
                newRecruits,
                skills
        );
    }
}
