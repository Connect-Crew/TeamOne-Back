package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.projectservice.project.domain.ProjectPart;
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
@Table(name = "part")
public class PartEntity {
    @Id
    private Long id;
    private Long project;
    private String partCategory;
    private String part;
    private String comment;
    private Long collected;
    private Long targetCollect;

    public ProjectPart toDomain() {
        return ProjectPart.builder()
                .id(id)
                .part(MemberPart.valueOf(part))
                .comment(comment)
                .current(collected)
                .max(targetCollect)
                .build();
    }

    public static PartEntity from(ProjectPart part, Long projectId) {
        return PartEntity.builder()
                .id(part.id())
                .project(projectId)
                .partCategory(part.part().getCategory().name())
                .part(part.part().name())
                .comment(part.comment())
                .collected(part.current())
                .targetCollect(part.max())
                .build();
    }

    public static List<PartEntity> from(List<ProjectPart> parts, Long projectId) {
        return parts.stream()
                .map(part -> from(part, projectId))
                .toList();
    }
}
