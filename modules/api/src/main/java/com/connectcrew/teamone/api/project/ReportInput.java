package com.connectcrew.teamone.api.project;

public record ReportInput(
        Long userId,
        Long projectId,
        String reason
) {
}
