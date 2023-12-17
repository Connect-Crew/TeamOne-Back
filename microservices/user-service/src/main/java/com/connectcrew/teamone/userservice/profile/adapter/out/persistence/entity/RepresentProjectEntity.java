package com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity;

import com.connectcrew.teamone.userservice.profile.domain.RepresentProject;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "represent_project")
public class RepresentProjectEntity {
    @Id
    private Long representId;

    private Long profileId;

    private Long projectId;

    public static RepresentProjectEntity fromDomain(RepresentProject representProject) {
        return RepresentProjectEntity.builder()
                .representId(representProject.id())
                .profileId(representProject.profileId())
                .projectId(representProject.projectId())
                .build();
    }

    public RepresentProject toDomain() {
        return RepresentProject.builder()
                .id(this.representId)
                .profileId(this.profileId)
                .projectId(this.projectId)
                .build();
    }
}
