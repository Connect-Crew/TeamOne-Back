package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity;

import com.connectcrew.teamone.projectservice.project.domain.Report;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report")
public class ReportEntity {

    @Id
    private Long id;
    private Long project;
    private Long user;
    private String reason;

    public static ReportEntity from(Report report) {
        return ReportEntity.builder()
                .project(report.projectId())
                .user(report.userId())
                .reason(report.reason())
                .build();
    }
}