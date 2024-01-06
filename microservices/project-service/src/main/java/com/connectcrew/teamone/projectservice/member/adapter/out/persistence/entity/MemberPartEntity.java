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
@Table(name = "member")
public class MemberPartEntity {

    @Id
    private Long id;

    private Long memberId;

    private Long partId;

    public MemberPart toDomain(Part part) {
        return new MemberPart(
                id,
                partId,
                memberId,
                part
        );
    }

    public static MemberPartEntity from(MemberPart part) {
        return MemberPartEntity.builder()
                .id(part.id())
                .memberId(part.memberId())
                .partId(part.partId())
                .build();
    }
}
