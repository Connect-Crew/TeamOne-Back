package com.connectcrew.teamone.api.profile;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfileResponse(
        Long id,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer responseRate,
        List<String> parts,
        List<Long> representProjects
) {
}
