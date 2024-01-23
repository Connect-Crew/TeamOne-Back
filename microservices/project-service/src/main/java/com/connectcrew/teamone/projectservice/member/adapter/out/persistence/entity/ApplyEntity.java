package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.api.projectservice.enums.ApplyState;
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

    private String contact;

    private ApplyState state;

    private String leaderMessage;

    public static ApplyEntity from(Apply apply) {
        return ApplyEntity.builder()
                .id(apply.id())
                .project(apply.projectId())
                .partId(apply.partId())
                .user(apply.userId())
                .message(apply.message())
                .contact(apply.contact())
                .state(apply.state())
                .leaderMessage(apply.leaderMessage())
                .build();
    }

    public Apply toDomain(MemberPart part) {
        return Apply.builder()
                .id(id)
                .projectId(project)
                .partId(partId)
                .part(part)
                .userId(user)
                .message(message)
                .contact(contact)
                .state(state)
                .leaderMessage(leaderMessage)
                .build();
    }
}
