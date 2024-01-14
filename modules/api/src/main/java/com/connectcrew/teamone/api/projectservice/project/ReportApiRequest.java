package com.connectcrew.teamone.api.projectservice.project;

import lombok.Builder;

@Builder
public record ReportApiRequest(
        Long userId,
        Long projectId,
        String reason
) {
}
