package com.connectcrew.teamone.userservice.entity;

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
}
