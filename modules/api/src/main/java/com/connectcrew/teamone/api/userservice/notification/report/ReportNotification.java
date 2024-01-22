package com.connectcrew.teamone.api.userservice.notification.report;

public record ReportNotification(
        Long id,
        Long userId,
        Long projectId,
        String projectTitle,
        String reason
) {

}
