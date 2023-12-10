package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity;

import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project")
public class ProjectEntity {

    @Id
    private Long id;

    private String title;

    private String introduction;

    private String chatRoomId;

    private Integer careerMin;

    private Integer careerMax;

    private Long leader;

    private Boolean withOnline;

    private String region;

    private LocalDateTime createdAt;

    private String state;

    private String goal;

    private Integer favorite;

    public static ProjectEntity from(Project project) {
        return ProjectEntity.builder()
                .id(project.id())
                .title(project.title())
                .introduction(project.introduction())
                .chatRoomId(project.chatRoomId())
                .careerMin(project.careerMin().getId())
                .careerMax(project.careerMax().getId())
                .leader(project.leader())
                .withOnline(project.online())
                .region(project.region().name())
                .createdAt(project.createdAt())
                .state(project.state().name())
                .goal(project.goal().name())
                .favorite(project.favorite())
                .build();
    }

    public Project toDomain(List<String> banners, List<RecruitStatus> recruitStatuses, List<String> skills, List<ProjectCategory> categories) {
        return Project.builder()
                .id(id)
                .title(title)
                .banners(banners)
                .region(Region.valueOf(region))
                .online(withOnline)
                .createdAt(createdAt)
                .state(ProjectState.valueOf(state))
                .careerMin(Career.valueOf(careerMin))
                .careerMax(Career.valueOf(careerMax))
                .chatRoomId(chatRoomId)
                .category(categories)
                .goal(ProjectGoal.valueOf(goal))
                .leader(leader)
                .introduction(introduction)
                .favorite(favorite)
                .recruitStatuses(recruitStatuses)
                .skills(skills)
                .build();
    }
}
