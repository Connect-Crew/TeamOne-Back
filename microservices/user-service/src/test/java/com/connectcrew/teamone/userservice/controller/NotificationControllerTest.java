package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.userservice.config.TestBeanConfig;
import com.connectcrew.teamone.userservice.notification.adapter.in.web.NotificationController;
import com.connectcrew.teamone.userservice.notification.adapter.out.messaging.FcmMessageAdapter;
import com.connectcrew.teamone.userservice.notification.adapter.out.persistence.FcmPersistenceAdapter;
import com.connectcrew.teamone.userservice.notification.adapter.out.persistence.entity.FcmEntity;
import com.connectcrew.teamone.userservice.notification.adapter.out.persistence.repository.FcmRepository;
import com.connectcrew.teamone.userservice.notification.application.NotificationAplService;
import com.connectcrew.teamone.userservice.notification.application.port.in.command.SendMessageCommand;
import com.connectcrew.teamone.userservice.notification.domain.FcmToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Import(TestBeanConfig.class)
@ExtendWith(SpringExtension.class)
@WebFluxTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private FcmRepository fcmRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendNotification() throws FirebaseMessagingException {
        FirebaseMessaging firebase = Mockito.mock(FirebaseMessaging.class);
        FcmPersistenceAdapter fcmAdapter = new FcmPersistenceAdapter(fcmRepository);
        FcmMessageAdapter messageAdapter = new FcmMessageAdapter(firebase, objectMapper);
        NotificationAplService service = new NotificationAplService(fcmAdapter, fcmAdapter, messageAdapter, null, null);

        when(fcmRepository.findAllByUserId(anyLong())).thenReturn(Flux.fromIterable(List.of(new FcmEntity(0L, 1L, "token1"), new FcmEntity(1L, 1L, "token2"))));
        when(firebase.send(any(Message.class))).thenReturn("test");

        Boolean result = service.sendMessage(new SendMessageCommand(0L, "title", "body", "deeplink")).block();

        assertThat(result).isTrue();
        verify(firebase, times(2)).send(any(Message.class));
    }

    @Test
    void notExistsTokenSendNotification() throws FirebaseMessagingException {
        FirebaseMessaging firebase = Mockito.mock(FirebaseMessaging.class);
        FcmPersistenceAdapter fcmAdapter = new FcmPersistenceAdapter(fcmRepository);
        FcmMessageAdapter messageAdapter = new FcmMessageAdapter(firebase, objectMapper);
        NotificationAplService service = new NotificationAplService(fcmAdapter, fcmAdapter, messageAdapter, null, null);

        when(fcmRepository.findAllByUserId(anyLong())).thenReturn(Flux.fromIterable(List.of(new FcmEntity(0L, 1L, "token1"), new FcmEntity(1L, 1L, "token2"))));
        when(firebase.send(any(Message.class))).thenAnswer(new Answer<String>() {
            private int count = 0;

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                // 호출 횟수에 따라 다른 값을 반환
                if (count == 0) {
                    count++;
                    return "First Call";
                } else {
                    throw Mockito.mock(FirebaseMessagingException.class);
                }
            }
        });
        when(fcmRepository.deleteById(anyLong())).thenReturn(Mono.just(true).then());

        Boolean result = service.sendMessage(new SendMessageCommand(0L, "title", "body", "deeplink")).block();

        assertThat(result).isTrue();
        verify(firebase, times(2)).send(any(Message.class));
        verify(fcmRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void saveFcmToken() {
        when(fcmRepository.save(any(FcmEntity.class))).thenReturn(Mono.just(FcmEntity.builder().build()));
        when(fcmRepository.findByUserIdAndToken(anyLong(), anyString())).thenReturn(Mono.just(new FcmEntity(0L, 1L, "fcm")));

        webTestClient.post()
                .uri("/notification/token")
                .bodyValue(new FcmToken(null, 0L, "fcm"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class);
    }
}