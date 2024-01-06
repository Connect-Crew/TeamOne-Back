package com.connectcrew.teamone.projectservice.project.domain;

import com.connectcrew.teamone.api.userservice.notification.report.ReportNotification;
import com.connectcrew.teamone.projectservice.project.domain.enums.ReportState;

public record Report(
        Long id,
        Long userId,
        Long projectId,
        String projectTitle,
        String reason,
        ReportState state
) {

    public Report(Long userId, Long projectId, String title, String reason) {
        this(null, userId, projectId, title, reason, ReportState.WAITING);
    }

    public ReportNotification toNotification() {
        return new ReportNotification(
                userId,
                projectId,
                reason
        );
    }
}
