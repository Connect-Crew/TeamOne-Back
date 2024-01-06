package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.project.ReportRequest;
import com.connectcrew.teamone.projectservice.project.domain.Report;

public record SaveReportCommand(
        Long userId,
        Long projectId,
        String reason
) {
    public static SaveReportCommand from(ReportRequest request) {
        return new SaveReportCommand(
                request.userId(),
                request.projectId(),
                request.reason()
        );
    }

    public Report toDomain(String projectTitle) {
        return new Report(userId, projectId, projectTitle, reason);
    }
}
