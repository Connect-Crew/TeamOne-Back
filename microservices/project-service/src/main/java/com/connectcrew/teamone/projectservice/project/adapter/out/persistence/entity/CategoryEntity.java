package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity;

import com.connectcrew.teamone.api.projectservice.enums.ProjectCategory;
import com.connectcrew.teamone.projectservice.project.domain.Category;
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

    public Category toDomain() {
        return new Category(id, ProjectCategory.valueOf(name));
    }

    public static CategoryEntity from(Category category, Long projectId) {
        return CategoryEntity.builder()
                .id(category.id())
                .project(projectId)
                .name(category.category().name())
                .build();
    }

    public static List<CategoryEntity> from(List<Category> categories, Long projectId) {
        return categories.stream()
                .map(category -> from(category, projectId))
                .toList();
    }
}
