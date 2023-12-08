package com.connectcrew.teamone.userservice.profile.domain;

import lombok.Builder;

@Builder
public record Part(
        Long partId,
        Long profileId,
        String part
) {
}
