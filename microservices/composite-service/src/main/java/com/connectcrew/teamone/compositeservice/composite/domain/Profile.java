package com.connectcrew.teamone.compositeservice.composite.domain;

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
        List<String> parts,
        List<Long> representProjects
) {
}
