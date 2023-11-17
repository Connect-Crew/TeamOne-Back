package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.user.favorite.FavoriteType;
import com.connectcrew.teamone.userservice.entity.FavoriteEntity;
import com.connectcrew.teamone.userservice.repository.*;
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

    @MockBean
    private RepresentProjectRepository representProjectRepository;

    @Test
    void getFavorites() {
        List<FavoriteEntity> favorites = List.of(
                new FavoriteEntity(0L, 0L, FavoriteType.PROJECT.name(), 1L),
                new FavoriteEntity(1L, 0L, FavoriteType.PROJECT.name(), 2L),
                new FavoriteEntity(2L, 0L, FavoriteType.PROJECT.name(), 3L)
        );
        List<Long> ids = List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L);

        when(favoriteRepository.findAllByProfileIdAndTypeAndTargetIn(anyLong(), anyString(), any(List.class))).thenReturn(Flux.fromIterable(favorites));

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
    void getFavorite() {
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
    void setFavorite() {
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
}