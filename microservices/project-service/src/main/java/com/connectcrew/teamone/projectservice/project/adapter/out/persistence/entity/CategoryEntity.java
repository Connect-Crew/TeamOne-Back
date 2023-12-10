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
@Table(name = "category")
public class CategoryEntity {
    @Id
    private Long id;
    private Long project;
    private String name;

    public static List<CategoryEntity> from(Project project, Long projectId) {
        return project.category().stream()
                .map(category -> CategoryEntity.builder()
                        .project(projectId)
                        .name(category.name())
                        .build())
                .toList();
    }
}
