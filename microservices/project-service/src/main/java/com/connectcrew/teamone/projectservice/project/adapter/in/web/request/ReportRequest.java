package com.connectcrew.teamone.projectservice.project.adapter.in.web.request;

import com.connectcrew.teamone.projectservice.project.application.port.in.command.ReportCommand;
import lombok.Builder;

@Builder
public record ReportRequest(
        Long userId,
        Long projectId,
        String reason
) {

    public ReportCommand toCommand() {
        return new ReportCommand(userId, projectId, reason);
    }
}
