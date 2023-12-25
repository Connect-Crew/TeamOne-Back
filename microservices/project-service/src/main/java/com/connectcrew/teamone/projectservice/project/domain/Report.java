package com.connectcrew.teamone.projectservice.project.domain;

import com.connectcrew.teamone.api.userservice.notification.report.ReportNotification;

public record Report(
        Long userId,
        Long projectId,
        String reason,
        ReportState state
) {

    public Report(Long userId, Long projectId, String reason) {
        this(userId, projectId, reason, ReportState.WAITING);
    }

    public ReportNotification toNotification() {
        return new ReportNotification(
                userId,
                projectId,
                reason
        );
    }
}
