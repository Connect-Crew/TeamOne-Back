package com.connectcrew.teamone.userservice.profile.domain.vo;

import com.connectcrew.teamone.userservice.profile.domain.Part;
import com.connectcrew.teamone.userservice.profile.domain.Profile;
import com.connectcrew.teamone.userservice.profile.domain.RepresentProject;

import java.util.List;

public record FullProfile(
        Profile profile,
        List<Part> parts,
        List<RepresentProject> representProjects
) {
}
