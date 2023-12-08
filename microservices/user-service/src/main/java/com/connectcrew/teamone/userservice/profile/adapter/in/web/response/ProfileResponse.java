package com.connectcrew.teamone.userservice.profile.adapter.in.web.response;

import com.connectcrew.teamone.userservice.profile.domain.Part;
import com.connectcrew.teamone.userservice.profile.domain.RepresentProject;
import com.connectcrew.teamone.userservice.profile.domain.vo.FullProfile;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileResponse(
        Long id,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer responseRate,
        List<String> parts,
        List<Long> representProjects
) {

    public static ProfileResponse from(FullProfile profile) {
        return ProfileResponse.builder()
                .id(profile.profile().id())
                .nickname(profile.profile().nickname())
                .profile(profile.profile().profile())
                .introduction(profile.profile().introduction())
                .temperature(profile.profile().temperature())
                .responseRate(profile.profile().recvApply() == 0 ? 0 : (int) (((double) profile.profile().resApply() / profile.profile().recvApply()) * 100))
                .parts(profile.parts().stream().map(Part::part).toList())
                .representProjects(profile.representProjects().stream().map(RepresentProject::projectId).toList())
                .build();
    }
}
