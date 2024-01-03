package com.connectcrew.teamone.projectservice.leader.application.port.in.query;

public record ProjectApplyStatusQuery(
        Long projectId,
        Long userId
) {
}
