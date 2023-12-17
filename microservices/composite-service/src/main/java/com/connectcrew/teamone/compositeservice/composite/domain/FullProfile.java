package com.connectcrew.teamone.compositeservice.composite.domain;

import java.util.List;

public record FullProfile(
        Long id,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer responseRate,
        List<String> parts,
        List<RepresentProject> representProjects
) {
    public static FullProfile from(Profile profile, List<RepresentProject> representProjects) {
        return new FullProfile(
                profile.id(),
                profile.nickname(),
                profile.profile(),
                profile.introduction(),
                profile.temperature(),
                profile.responseRate(),
                profile.parts(),
                representProjects
        );
    }
}
