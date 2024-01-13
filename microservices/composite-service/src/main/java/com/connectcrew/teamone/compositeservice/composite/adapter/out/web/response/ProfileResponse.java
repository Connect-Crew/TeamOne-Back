package com.connectcrew.teamone.compositeservice.composite.adapter.out.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.Profile;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;

import java.util.List;

public record ProfileResponse(
        Long id,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer responseRate,
        List<MemberPart> parts,
        List<Long> representProjects
) {
    public Profile toDomain() {
        return new Profile(id, nickname, profile, introduction, temperature, responseRate, parts, representProjects);
    }
}
