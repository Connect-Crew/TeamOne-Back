package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity;

import com.connectcrew.teamone.api.projectservice.enums.Part;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.enums.ApplyState;
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
    private ApplyState state;

    public static ApplyEntity from(Apply apply) {
        return ApplyEntity.builder()
                .id(apply.id())
                .project(apply.projectId())
                .partId(apply.partId())
                .user(apply.userId())
                .message(apply.message())
                .state(apply.state())
                .build();
    }

    public Apply toDomain(Part part) {
        return Apply.builder()
                .id(id)
                .projectId(project)
                .partId(partId)
                .part(part)
                .userId(user)
                .message(message)
                .state(state)
                .build();
    }
}
