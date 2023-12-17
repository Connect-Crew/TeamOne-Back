package com.connectcrew.teamone.compositeservice.composite.domain;

public record Report(
        Long userId,
        Long projectId,
        String reason
) {
}
