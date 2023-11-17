package com.connectcrew.teamone.compositeservice.service;

import com.connectcrew.teamone.compositeservice.request.ProjectRequest;
import com.connectcrew.teamone.compositeservice.request.UserRequest;
import com.connectcrew.teamone.compositeservice.resposne.ProfileRes;
import com.connectcrew.teamone.compositeservice.resposne.RepresentProjectRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRequest userRequest;
    private final ProjectRequest projectRequest;

    private final BannerService bannerService;

    public Mono<ProfileRes> getProfileRes(Long id) {
        return userRequest.getProfile(id)
                .flatMap(leaderProfile -> getRepresentProjectRes(leaderProfile.representProjects())
                        .map(representProjects -> ProfileRes.builder()
                                .id(leaderProfile.id())
                                .nickname(leaderProfile.nickname())
                                .profile(leaderProfile.profile())
                                .introduction(leaderProfile.introduction())
                                .temperature(leaderProfile.temperature())
                                .responseRate(leaderProfile.responseRate())
                                .parts(leaderProfile.parts())
                                .representProjects(representProjects)
                                .build()));
    }

    private Mono<List<RepresentProjectRes>> getRepresentProjectRes(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> projectRequest.getProjectThumbnail(id)
                        .map(thumbnail -> new RepresentProjectRes(id, bannerService.getBannerUrlPath(thumbnail)))
                        .defaultIfEmpty(new RepresentProjectRes(id, null))
                )
                .collectList();
    }
}
