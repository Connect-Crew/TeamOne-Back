package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.api.profile.ProfileResponse;
import com.connectcrew.teamone.userservice.config.TestBeanConfig;
import com.connectcrew.teamone.userservice.profile.adapter.in.web.ProfileController;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity.PartEntity;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity.ProfileEntity;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity.RepresentProjectEntity;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository.PartRepository;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository.ProfileRepository;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository.RepresentProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Import(TestBeanConfig.class)
@ExtendWith(SpringExtension.class)
@WebFluxTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private RepresentProjectRepository representProjectRepository;


    @Test
    void getProfile() {
        ProfileEntity profile = ProfileEntity.builder()
                .userId(1L)
                .nickname("testNick")
                .profile("testProfile")
                .introduction("testIntroduction")
                .temperature(36.5)
                .recvApply(1)
                .resApply(1)
                .build();

        List<PartEntity> parts = List.of(
                new PartEntity(0L, 0L, "testPart1"),
                new PartEntity(1L, 1L, "testPart2")
        );

        List<RepresentProjectEntity> representProjects = List.of(
                new RepresentProjectEntity(0L, 0L, 1L),
                new RepresentProjectEntity(1L, 0L, 2L)
        );

        when(profileRepository.findByUserId(anyLong())).thenReturn(Mono.just(profile));
        when(partRepository.findAllByProfileId(anyLong())).thenReturn(Flux.fromIterable(parts));
        when(representProjectRepository.findAllByProfileId(anyLong())).thenReturn(Flux.fromIterable(representProjects));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/profile/")
                        .queryParam("id", "123")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProfileResponse.class);
    }

    @Test
    void notFoundProfile() {
        when(profileRepository.findByUserId(anyLong())).thenReturn(Mono.empty());

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/profile/")
                        .queryParam("id", "123")
                        .build())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorInfo.class);
    }
}