package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.projectservice.project.domain.Report;

public record ReportCommand(
        Long userId,
        Long projectId,
        String reason
) {

    public Report toDomain() {
        return new Report(userId, projectId, reason);
    }
}
