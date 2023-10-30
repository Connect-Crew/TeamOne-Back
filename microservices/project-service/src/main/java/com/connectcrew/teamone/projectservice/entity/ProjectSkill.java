package com.connectcrew.teamone.projectservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSkill {
    @Id
    private Long id;
    private Long project;
    private Long skill;
}
