package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.FullProfile;

import java.util.List;

public record ProfileResponse(
        Long id,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer responseRate,
        List<PartResponse> parts,
        List<RepresentProjectResponse> representProjects
) {
    public static ProfileResponse from(FullProfile profile) {
        return new ProfileResponse(
                profile.id(),
                profile.nickname(),
                profile.profile(),
                profile.introduction(),
                profile.temperature(),
                profile.responseRate(),
                profile.parts().stream().map(PartResponse::new).toList(),
                profile.representProjects().stream()
                        .map(RepresentProjectResponse::from)
                        .toList()
        );
    }
}
