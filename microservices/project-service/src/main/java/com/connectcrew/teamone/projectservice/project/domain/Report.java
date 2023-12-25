package com.connectcrew.teamone.projectservice.project.domain;

import com.connectcrew.teamone.api.userservice.notification.report.ReportNotification;

public record Report(
        Long userId,
        Long projectId,
        String reason
) {

    public ReportNotification toNotification() {
        return new ReportNotification(
                userId,
                projectId,
                reason
        );
    }
}
