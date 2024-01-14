package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.userservice.profile.ProfileApiResponse;
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

    public static Profile of(ProfileApiResponse res) {
        return Profile.builder()
                .id(res.id())
                .nickname(res.nickname())
                .profile(res.profile())
                .introduction(res.introduction())
                .temperature(res.temperature())
                .responseRate(res.responseRate())
                .parts(res.parts())
                .representProjects(res.representProjects())
                .build();
    }
}
