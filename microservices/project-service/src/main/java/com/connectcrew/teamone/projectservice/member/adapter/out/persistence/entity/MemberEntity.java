package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity;

import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.member.domain.ProjectMemberPart;
import com.connectcrew.teamone.projectservice.member.domain.enums.MemberState;
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
@Table(name = "member")
public class MemberEntity {

    @Id
    private Long id;
    private Long user;
    private Long projectId;
    private MemberState state;

    public Member toDomain(List<ProjectMemberPart> parts) {
        return new Member(
                id,
                user,
                projectId,
                parts,
                state
        );
    }

    public static MemberEntity from(Member member) {
        return MemberEntity.builder()
                .id(member.id())
                .user(member.user())
                .projectId(member.project())
                .state(member.state())
                .build();
    }
}
