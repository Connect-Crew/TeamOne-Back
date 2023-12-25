package com.connectcrew.teamone.userservice.profile.domain.vo;

import com.connectcrew.teamone.api.userservice.profile.ProfileResponse;
import com.connectcrew.teamone.userservice.profile.domain.Part;
import com.connectcrew.teamone.userservice.profile.domain.Profile;
import com.connectcrew.teamone.userservice.profile.domain.RepresentProject;

import java.util.List;

public record FullProfile(
        Profile profile,
        List<Part> parts,
        List<RepresentProject> representProjects
) {
    public ProfileResponse toResponse() {
        return ProfileResponse.builder()
                .id(profile().id())
                .nickname(profile().nickname())
                .profile(profile().profile())
                .introduction(profile().introduction())
                .temperature(profile().temperature())
                .responseRate(profile().responseRate())
                .parts(parts().stream().map(Part::part).toList())
                .representProjects(representProjects().stream().map(RepresentProject::projectId).toList())
                .build();
    }
}
