package com.connectcrew.teamone.api.projectservice.project;

import lombok.Builder;

@Builder
public record ReportRequest(
        Long userId,
        Long projectId,
        String reason
) {
}
