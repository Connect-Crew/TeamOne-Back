package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.user.notification.FcmNotification;
import com.connectcrew.teamone.userservice.entity.FcmEntity;
import com.connectcrew.teamone.userservice.repository.*;
import com.google.firebase.messaging.FirebaseMessaging;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(NotificationController.class)
class NotificationControllerTest {

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

    @MockBean
    private FcmRepository fcmRepository;

    @MockBean
    private FirebaseMessaging firebaseMessaging;


    @Test
    void sendNotification() {
        when(fcmRepository.findAllByUserId(anyLong())).thenReturn(Flux.fromIterable(List.of(new FcmEntity(0L, 1L, "token1"), new FcmEntity(1L, 1L, "token2"))));

        webTestClient.post()
                .uri("/notification")
                .bodyValue(new FcmNotification(0L, "title", "body"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class);
    }
}