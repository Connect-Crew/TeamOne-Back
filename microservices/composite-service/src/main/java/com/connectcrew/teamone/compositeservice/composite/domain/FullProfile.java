package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;

import java.util.List;

public record FullProfile(
        Long id,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer responseRate,
        List<MemberPart> parts,
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

    public FullProfile update(List<MemberPart> parts) {
        return new FullProfile(
                this.id,
                this.nickname,
                this.profile,
                this.introduction,
                this.temperature,
                this.responseRate,
                parts,
                this.representProjects
        );
    }
}
