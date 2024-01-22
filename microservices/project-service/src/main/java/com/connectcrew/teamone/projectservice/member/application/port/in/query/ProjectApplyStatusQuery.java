package com.connectcrew.teamone.projectservice.member.application.port.in.query;

public record ProjectApplyStatusQuery(
        Long projectId,
        Long userId
) {
}
