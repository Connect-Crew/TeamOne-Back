package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.projectservice.project.ReportApiRequest;

public record Report(
        Long userId,
        Long projectId,
        String reason
) {

    public ReportApiRequest toApiRequest() {
        return new ReportApiRequest(
                userId,
                projectId,
                reason
        );
    }
}
