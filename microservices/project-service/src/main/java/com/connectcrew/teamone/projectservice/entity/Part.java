package com.connectcrew.teamone.projectservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Part {
    @Id
    private Long id;
    private Long project;
    private String part;
    private Integer careerMin;
    private Integer careerMax;
    private String comment;
    private Integer collected;
    private Integer targetCollect;
}
