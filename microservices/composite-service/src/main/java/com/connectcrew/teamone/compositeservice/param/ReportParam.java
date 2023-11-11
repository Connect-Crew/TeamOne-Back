package com.connectcrew.teamone.compositeservice.param;

import com.connectcrew.teamone.api.project.ReportInput;

public record ReportParam(
        Long projectId,
        String reason
) {

    public ReportInput toInput(Long userId) {
        return new ReportInput(userId, projectId, reason);
    }
}
