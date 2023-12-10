package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity;

import com.connectcrew.teamone.projectservice.project.domain.Project;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "skill")
public class SkillEntity {
    @Id
    private Long id;
    private Long project;
    private String name;

    public static List<SkillEntity> from(Project project, Long projectId) {
        return project.skills().stream()
                .map(skill -> SkillEntity.builder()
                        .project(projectId)
                        .name(skill)
                        .build())
                .toList();
    }
}
