package com.connectcrew.teamone.api.user.profile;

import lombok.Builder;

import java.util.List;

@Builder
public record Profile(
        Long id,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer responseRate,
        List<String> parts
) {
}
