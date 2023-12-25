package com.connectcrew.teamone.api.userservice.notification.report;

public record ReportNotification(
        Long userId,
        Long projectId,
        String reason
) {

}
