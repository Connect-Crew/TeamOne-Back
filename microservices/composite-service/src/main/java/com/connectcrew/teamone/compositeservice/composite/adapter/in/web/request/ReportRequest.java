package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import com.connectcrew.teamone.compositeservice.composite.domain.Report;

public record ReportRequest(
        Long projectId,
        String reason
) {

    public Report toDomain(Long userId) {
        return new Report(userId, projectId, reason);
    }
}
