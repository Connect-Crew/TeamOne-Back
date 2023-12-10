package com.connectcrew.teamone.projectservice.project.domain;

public record Report(
        Long userId,
        Long projectId,
        String reason
) {
}
