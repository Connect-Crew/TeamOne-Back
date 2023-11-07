package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.favorite.FavoriteType;
import com.connectcrew.teamone.userservice.entity.FavoriteEntity;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(FavoriteController.class)
class FavoriteControllerTest {
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

    private ProfileEntity profile = ProfileEntity.builder()
            .profileId(1L)
            .userId(1L)
            .nickname("testNick")
            .profile("testProfile")
            .introduction("testIntroduction")
            .temperature(36.5)
            .recvApply(1)
            .resApply(1)
            .build();

    @Test
    void getFavorites() {
        List<FavoriteEntity> favorites = List.of(
                new FavoriteEntity(0L, 0L, FavoriteType.PROJECT.name(), 1L),
                new FavoriteEntity(1L, 0L, FavoriteType.PROJECT.name(), 2L),
                new FavoriteEntity(2L, 0L, FavoriteType.PROJECT.name(), 3L)
        );
        List<Long> ids = List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L);

        when(profileRepository.findByUserId(anyLong())).thenReturn(Mono.just(profile));
        when(favoriteRepository.findAllByProfileIdAndTypeAndTargetIn(anyLong(), anyString(), any(Flux.class))).thenReturn(Flux.fromIterable(favorites));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/favorite/favorites")
                        .queryParam("userId", "123456789")
                        .queryParam("type", FavoriteType.PROJECT.name())
                        .queryParam("ids", ids)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Map<Long, Boolean>>() {
                })
                .consumeWith(result -> {
                    Map<Long, Boolean> body = result.getResponseBody();

                    assert body != null;
                    assertEquals(7, body.keySet().size());
                    assertFalse(body.get(0L));
                    assertTrue(body.get(1L));
                    assertTrue(body.get(2L));
                    assertTrue(body.get(3L));
                    assertFalse(body.get(4L));
                    assertFalse(body.get(5L));
                    assertFalse(body.get(6L));
                });
    }


    @Test
    void notfoundGetFavorites() {
        List<Long> ids = List.of(1L, 2L, 3L, 4L, 5L, 6L);
        when(profileRepository.findByUserId(anyLong())).thenReturn(Mono.empty());

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/favorite/favorites")
                        .queryParam("userId", "123456789")
                        .queryParam("type", FavoriteType.PROJECT.name())
                        .queryParam("ids", ids)
                        .build())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorInfo.class);
    }

    @Test
    void getFavorite() {
        when(profileRepository.findByUserId(anyLong())).thenReturn(Mono.just(profile));
        when(favoriteRepository.existsByProfileIdAndTypeAndTarget(anyLong(), anyString(), anyLong())).thenReturn(Mono.just(true));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/favorite/")
                        .queryParam("userId", "123456789")
                        .queryParam("type", FavoriteType.PROJECT.name())
                        .queryParam("target", "1")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class);
    }

    @Test
    void notfoundGetFavorite() {
        when(profileRepository.findByUserId(anyLong())).thenReturn(Mono.empty());

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/favorite/")
                        .queryParam("userId", "123456789")
                        .queryParam("type", FavoriteType.PROJECT.name())
                        .queryParam("target", "1")
                        .build())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorInfo.class);
    }

    @Test
    void setFavorite() {
        when(profileRepository.findByUserId(anyLong())).thenReturn(Mono.just(profile));
        when(favoriteRepository.existsByProfileIdAndTypeAndTarget(anyLong(), anyString(), anyLong())).thenReturn(Mono.just(false));
        when(favoriteRepository.save(any(FavoriteEntity.class))).thenReturn(Mono.just(new FavoriteEntity(0L, 0L, FavoriteType.PROJECT.name(), 1L)));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/favorite/")
                        .queryParam("userId", "123456789")
                        .queryParam("type", FavoriteType.PROJECT.name())
                        .queryParam("target", "1")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .consumeWith(result -> assertEquals(Boolean.TRUE, result.getResponseBody()));
    }

    @Test
    void cancelFavorite() {
        when(profileRepository.findByUserId(anyLong())).thenReturn(Mono.just(profile));
        when(favoriteRepository.existsByProfileIdAndTypeAndTarget(anyLong(), anyString(), anyLong())).thenReturn(Mono.just(true));
        when(favoriteRepository.deleteByProfileIdAndTypeAndTarget(anyLong(), anyString(), anyLong())).thenReturn(Mono.just(true));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/favorite/")
                        .queryParam("userId", "123456789")
                        .queryParam("type", FavoriteType.PROJECT.name())
                        .queryParam("target", "1")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .consumeWith(result -> assertEquals(Boolean.FALSE, result.getResponseBody()));
    }

    @Test
    void notfoundSetFavorite() {
        when(profileRepository.findByUserId(anyLong())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/favorite/")
                        .queryParam("userId", "123456789")
                        .queryParam("type", FavoriteType.PROJECT.name())
                        .queryParam("target", "1")
                        .build())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorInfo.class);
    }
}