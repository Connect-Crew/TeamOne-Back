package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity;

import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;
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
    private Integer collected;
    private Integer targetCollect;

    public RecruitStatus toDomain() {
        return RecruitStatus.builder()
                .id(id)
                .part(MemberPart.valueOf(part))
                .comment(comment)
                .current(collected)
                .max(targetCollect)
                .build();
    }

    public static List<PartEntity> from(Project project, Long projectId) {
        return project.recruitStatuses().stream()
                .map(recruitStatus -> PartEntity.builder()
                        .id(recruitStatus.id())
                        .project(projectId)
                        .partCategory(recruitStatus.part().getCategory().name())
                        .part(recruitStatus.part().name())
                        .comment(recruitStatus.comment())
                        .collected(recruitStatus.current())
                        .targetCollect(recruitStatus.max())
                        .build())
                .toList();
    }
}
