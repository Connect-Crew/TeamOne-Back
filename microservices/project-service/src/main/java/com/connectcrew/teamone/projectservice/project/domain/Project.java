package com.connectcrew.teamone.projectservice.project.domain;

import com.connectcrew.teamone.api.projectservice.enums.Career;
import com.connectcrew.teamone.api.projectservice.enums.ProjectGoal;
import com.connectcrew.teamone.api.projectservice.enums.ProjectState;
import com.connectcrew.teamone.api.projectservice.enums.Region;
import com.connectcrew.teamone.api.projectservice.project.ProjectApiResponse;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.member.domain.ProjectMemberPart;
import com.connectcrew.teamone.projectservice.project.domain.vo.UserRelationWithProject;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record Project(
        Long id,
        String title,
        List<Banner> banners,
        Region region,
        Boolean online,
        ProjectState state,
        UUID chatRoomId,
        Career careerMin,
        Career careerMax,
        Long leader,
        List<Category> category,
        ProjectGoal goal,
        String introduction,
        List<ProjectPart> parts,
        Integer favorite,
        List<Skill> skills,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ProjectApiResponse toResponse(Member leader, UserRelationWithProject user) {
        return ProjectApiResponse.builder()
                .id(id)
                .title(title)
                .banners(banners.stream().map(Banner::path).toList())
                .region(region)
                .online(online)
                .createdAt(createdAt)
                .state(state)
                .careerMin(careerMin)
                .careerMax(careerMax)
                .chatRoomId(chatRoomId)
                .category(category.stream().map(Category::category).toList())
                .goal(goal)
                .leader(leader.user())
                .leaderParts(leader.parts().stream().map(ProjectMemberPart::part).toList())
                .introduction(introduction)
                .favorite(favorite)
                .recruitStatuses(parts.stream().map(r -> r.toResponse(user.applies().contains(r.part()) || user.members().contains(r.part()))).toList())
                .skills(skills.stream().map(Skill::skill).toList())
                .build();
    }

}
