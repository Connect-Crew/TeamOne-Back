package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.project.ReportRequest;
import com.connectcrew.teamone.projectservice.project.domain.Report;

public record ReportCommand(
        Long userId,
        Long projectId,
        String reason
) {
    public static ReportCommand from(ReportRequest request) {
        return new ReportCommand(
                request.userId(),
                request.projectId(),
                request.reason()
        );
    }

    public Report toDomain() {
        return new Report(userId, projectId, reason);
    }
}
