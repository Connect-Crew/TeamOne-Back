package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_part")
public class MemberPartEntity {

    @Id
    private Long id;

    private Long member;

    private Long part;

    public com.connectcrew.teamone.projectservice.member.domain.MemberPart toDomain(MemberPart part) {
        return new com.connectcrew.teamone.projectservice.member.domain.MemberPart(
                id,
                this.part,
                member,
                part
        );
    }

    public static MemberPartEntity from(com.connectcrew.teamone.projectservice.member.domain.MemberPart part, Long memberId) {
        return MemberPartEntity.builder()
                .id(part.id())
                .member(memberId)
                .part(part.partId())
                .build();
    }
}
