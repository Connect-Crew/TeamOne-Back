package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.api.user.profile.Profile;
import com.connectcrew.teamone.userservice.entity.PartEntity;
import com.connectcrew.teamone.userservice.entity.RepresentProjectEntity;
import com.connectcrew.teamone.userservice.repository.PartRepository;
import com.connectcrew.teamone.userservice.repository.ProfileRepository;
import com.connectcrew.teamone.userservice.repository.RepresentProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileRepository profileRepository;
    private final PartRepository partRepository;

    private final RepresentProjectRepository representProjectRepository;

    @GetMapping("/")
    Mono<Profile> getProfile(Long id) {
        return profileRepository.findByUserId(id)
                .switchIfEmpty(Mono.error(new NotFoundException("프로필을 찾지 못했습니다.")))
                .flatMap(profileEntity -> partRepository.findAllByProfileId(id).collectList().map(partEntity -> Tuples.of(profileEntity, partEntity)))
                .flatMap(tuple -> representProjectRepository.findAllByProfileId(id).collectList().map(projects -> Tuples.of(tuple.getT1(), tuple.getT2(), projects)))
                .map(tuple -> Profile.builder()
                        .id(tuple.getT1().getUserId())
                        .nickname(tuple.getT1().getNickname())
                        .profile(tuple.getT1().getProfile())
                        .introduction(tuple.getT1().getIntroduction())
                        .temperature(tuple.getT1().getTemperature())
                        .responseRate(tuple.getT1().getRecvApply() == 0 ? 0 : (int) (((double) tuple.getT1().getResApply() / tuple.getT1().getRecvApply()) * 100))
                        .parts(tuple.getT2().stream().map(PartEntity::getPart).toList())
                        .representProjects(tuple.getT3().stream().map(RepresentProjectEntity::getProjectId).toList())
                        .build());
    }
}
