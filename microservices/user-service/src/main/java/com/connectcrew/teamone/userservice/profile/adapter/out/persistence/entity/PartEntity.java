package com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity;

import com.connectcrew.teamone.userservice.profile.domain.Part;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "part")
public class PartEntity {

    @Id
    private Long partId;

    private Long profileId;

    private String part;

    public static PartEntity fromDomain(Part part) {
        return PartEntity.builder()
                .partId(part.partId())
                .profileId(part.profileId())
                .part(part.part())
                .build();
    }

    public Part toDomain() {
        return Part.builder()
                .partId(this.partId)
                .profileId(this.profileId)
                .part(this.part)
                .build();
    }
}
