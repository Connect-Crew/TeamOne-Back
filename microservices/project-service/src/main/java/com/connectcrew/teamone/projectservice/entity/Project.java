package com.connectcrew.teamone.projectservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    private Long id;

    private String title;

    private String introduction;

    private Integer careerMin;

    private Integer careerMax;

    private Long leader;

    private Boolean withOnline;

    private String region;

    private LocalDateTime createdAt;

    private LocalDate startDate;

    private LocalDate endDate;

    private String state;

    private String goal;

    private Integer favorite;
}
