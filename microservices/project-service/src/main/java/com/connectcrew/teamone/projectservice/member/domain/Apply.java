package com.connectcrew.teamone.projectservice.member.domain;

import lombok.Builder;

@Builder
public record Apply(
        Long id,
        Long userId,
        Long projectId,
        Long part,
        String message
) {
}
