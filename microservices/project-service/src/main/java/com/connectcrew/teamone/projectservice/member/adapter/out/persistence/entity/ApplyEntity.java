package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity;

import com.connectcrew.teamone.projectservice.member.domain.Apply;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "apply")
public class ApplyEntity {
    @Id
    private Long id;

    private Long project;

    private Long partId;

    private Long user;

    private String message;

    public static ApplyEntity from(Apply apply) {
        return ApplyEntity.builder()
                .id(apply.id())
                .project(apply.projectId())
                .partId(apply.part())
                .user(apply.userId())
                .message(apply.message())
                .build();
    }

    public Apply toDomain() {
        return Apply.builder()
                .id(id)
                .projectId(project)
                .part(partId)
                .userId(user)
                .message(message)
                .build();
    }
}
