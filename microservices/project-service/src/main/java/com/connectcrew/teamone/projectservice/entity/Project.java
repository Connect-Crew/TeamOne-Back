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

    private String thumbnail;

    private String memberIntroduction;

    private Long leader;

    private Boolean withOnline;

    private LocalDateTime createdAt;

    private LocalDate startDate;

    private LocalDate endDate;

    private String state;

    private String goal;
}
