package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity;

import com.connectcrew.teamone.api.projectservice.enums.Part;
import com.connectcrew.teamone.projectservice.member.domain.MemberPart;
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

    public MemberPart toDomain(Part part) {
        return new MemberPart(
                id,
                this.part,
                member,
                part
        );
    }

    public static MemberPartEntity from(MemberPart part, Long memberId) {
        return MemberPartEntity.builder()
                .id(part.id())
                .member(memberId)
                .part(part.partId())
                .build();
    }
}
