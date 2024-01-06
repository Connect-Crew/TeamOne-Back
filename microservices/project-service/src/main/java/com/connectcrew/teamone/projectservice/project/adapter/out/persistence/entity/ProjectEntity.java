package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity;

import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.projectservice.project.domain.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private LocalDateTime updatedAt;

    private String state;

    private String goal;

    private Integer favorite;

    public static ProjectEntity from(Project project) {
        return ProjectEntity.builder()
                .id(project.id())
                .title(project.title())
                .introduction(project.introduction())
                .chatRoomId(project.chatRoomId().toString())
                .careerMin(project.careerMin().getId())
                .careerMax(project.careerMax().getId())
                .leader(project.leader())
                .withOnline(project.online())
                .region(project.region().name())
                .createdAt(project.createdAt())
                .updatedAt(project.updatedAt())
                .state(project.state().name())
                .goal(project.goal().name())
                .favorite(project.favorite())
                .build();
    }

    public Project toDomain(List<Banner> banners, List<ProjectPart> parts, List<Skill> skills, List<Category> categories) {
        return Project.builder()
                .id(id)
                .title(title)
                .banners(banners)
                .region(Region.valueOf(region))
                .online(withOnline)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .state(ProjectState.valueOf(state))
                .careerMin(Career.valueOf(careerMin))
                .careerMax(Career.valueOf(careerMax))
                .chatRoomId(UUID.fromString(chatRoomId))
                .category(categories)
                .goal(ProjectGoal.valueOf(goal))
                .leader(leader)
                .introduction(introduction)
                .favorite(favorite)
                .parts(parts)
                .skills(skills)
                .build();
    }
}
