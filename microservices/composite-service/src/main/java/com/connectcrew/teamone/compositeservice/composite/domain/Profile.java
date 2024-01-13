package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;
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
        List<MemberPart> parts,
        List<Long> representProjects
) {
}
