package com.connectcrew.teamone.api.userservice.profile;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileApiResponse(
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
