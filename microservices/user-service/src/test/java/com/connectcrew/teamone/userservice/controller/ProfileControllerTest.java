package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.profile.Profile;
import com.connectcrew.teamone.userservice.entity.PartEntity;
import com.connectcrew.teamone.userservice.entity.ProfileEntity;
import com.connectcrew.teamone.userservice.repository.FavoriteRepository;
import com.connectcrew.teamone.userservice.repository.PartRepository;
import com.connectcrew.teamone.userservice.repository.ProfileRepository;
import com.connectcrew.teamone.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private FavoriteRepository favoriteRepository;

    @MockBean
    private PartRepository partRepository;

    @Test
    void getProfile() {
        ProfileEntity profile = ProfileEntity.builder()
                .profileId(1L)
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

        when(profileRepository.findByProfileId(anyLong())).thenReturn(Mono.just(profile));
        when(partRepository.findAllByProfileId(anyLong())).thenReturn(Flux.fromIterable(parts));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/profile/")
                        .queryParam("id", "123")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Profile.class);
    }

    @Test
    void notFoundProfile() {
        when(profileRepository.findByProfileId(anyLong())).thenReturn(Mono.empty());

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