package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.api.user.Role;
import com.connectcrew.teamone.api.user.Social;
import com.connectcrew.teamone.api.user.UserRegisterRequest;
import com.connectcrew.teamone.api.user.UserResponse;
import com.connectcrew.teamone.userservice.config.TestBeanConfig;
import com.connectcrew.teamone.userservice.notification.adapter.out.persistence.entity.FcmEntity;
import com.connectcrew.teamone.userservice.notification.adapter.out.persistence.repository.FcmRepository;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity.ProfileEntity;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository.ProfileRepository;
import com.connectcrew.teamone.userservice.user.adapter.in.web.AuthController;
import com.connectcrew.teamone.userservice.user.adapter.out.persistence.entity.UserEntity;
import com.connectcrew.teamone.userservice.user.adapter.out.persistence.repository.UserRepository;
import com.connectcrew.teamone.userservice.user.domain.enums.UserExceptionMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Import(TestBeanConfig.class)
@ExtendWith(SpringExtension.class)
@WebFluxTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private FcmRepository fcmRepository;


    @Test
    void find() {
        UserEntity user = UserEntity.builder()
                .id(10L)
                .provider(Social.GOOGLE.name())
                .socialId("123456789")
                .username("testName")
                .email("email@google.com")
                .role(Role.USER.name())
                .termsAgreement(true)
                .privacyAgreement(true)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        when(userRepository.findBySocialIdAndProvider(anyString(), anyString())).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/")
                        .queryParam("socialId", "123456789")
                        .queryParam("provider", "GOOGLE")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class);
    }

    @Test
    void notfoundFind() {
        when(userRepository.findBySocialIdAndProvider(anyString(), anyString())).thenReturn(Mono.empty());

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/")
                        .queryParam("socialId", "123456789")
                        .queryParam("provider", "GOOGLE")
                        .build())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorInfo.class);
    }

    static Stream<Arguments> registerFailArgs() {
        return Stream.of(
                Arguments.of(new UserRegisterRequest("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", false, true, "fcm"), UserExceptionMessage.TERMS_AGREEMENT_REQUIRED.getMessage()),
                Arguments.of(new UserRegisterRequest("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", true, false, "fcm"), UserExceptionMessage.PRIVACY_AGREEMENT_REQUIRED.getMessage()),
                Arguments.of(new UserRegisterRequest("TestSocial", Social.GOOGLE, "TestUser", "T", null, "Test@Test.com", true, true, "fcm"), UserExceptionMessage.NAME_LENGTH_LESS_2.getMessage()),
                Arguments.of(new UserRegisterRequest("TestSocial", Social.GOOGLE, "TestUser", "TestNickOver10Length", null, "Test@Test.com", true, true, "fcm"), UserExceptionMessage.NAME_LENGTH_OVER_10.getMessage()),
                Arguments.of(new UserRegisterRequest("TestSocial", Social.GOOGLE, "TestUser", "Test Nick", null, "Test@Test.com", true, true, "fcm"), UserExceptionMessage.SPACE_OR_SPECIAL_CHARACTER_IN_NAME.getMessage()),
                Arguments.of(new UserRegisterRequest("TestSocial", Social.GOOGLE, "TestUser", "dupNick", null, "Test@Test.com", true, true, "fcm"), UserExceptionMessage.DUPLICATE_NICKNAME.getMessage())
        );
    }

    @ParameterizedTest
    @MethodSource("registerFailArgs")
    void registerFailTest(UserRegisterRequest param, String expectMessage) {
        UserEntity user = UserEntity.builder()
                .id(10L)
                .provider(Social.GOOGLE.name())
                .socialId("123456789")
                .username("testName")
                .email("email@google.com")
                .role(Role.USER.name())
                .termsAgreement(true)
                .privacyAgreement(true)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        when(profileRepository.existsByNickname(anyString())).thenReturn(Mono.just(false));
        when(profileRepository.existsByNickname("dupNick")).thenReturn(Mono.just(true));
        when(userRepository.existsBySocialIdAndProvider(anyString(), anyString())).thenReturn(Mono.just(false));
        when(userRepository.save(any(UserEntity.class))).thenReturn(Mono.just(user));
        when(profileRepository.save(any(ProfileEntity.class))).thenReturn(Mono.just(ProfileEntity.builder().build()));

        webTestClient.post()
                .uri("/user/")
                .body(Mono.just(param), UserRegisterRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class)
                .value(errorInfo -> {
                    assert errorInfo.getMessage().equals(expectMessage);
                });
    }

    // 중복 회원가입 테스트
    @Test
    void duplicateRegisterTest() {
        UserRegisterRequest param = new UserRegisterRequest("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", true, true, "fcm");
        UserEntity user = UserEntity.builder()
                .id(10L)
                .provider(Social.GOOGLE.name())
                .socialId("123456789")
                .username("testName")
                .email("email@google.com")
                .role(Role.USER.name())
                .termsAgreement(true)
                .privacyAgreement(true)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        when(profileRepository.existsByNickname(anyString())).thenReturn(Mono.just(false));
        when(userRepository.existsBySocialIdAndProvider(anyString(), anyString())).thenReturn(Mono.just(true));
        when(userRepository.save(any(UserEntity.class))).thenReturn(Mono.just(user));
        when(profileRepository.save(any(ProfileEntity.class))).thenReturn(Mono.just(ProfileEntity.builder().build()));

        webTestClient.post()
                .uri("/user/")
                .body(Mono.just(param), UserRegisterRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class)
                .value(errorInfo -> {
                    assert errorInfo.getMessage().equals(UserExceptionMessage.ALREADY_EXISTS_USER.getMessage());
                });
    }

    @Test
    void registerTest() {
        UserRegisterRequest param = new UserRegisterRequest("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", true, true, "fcm");
        UserEntity user = UserEntity.builder()
                .id(10L)
                .provider(Social.GOOGLE.name())
                .socialId("123456789")
                .username("testName")
                .email("email@google.com")
                .role(Role.USER.name())
                .termsAgreement(true)
                .privacyAgreement(true)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        when(profileRepository.existsByNickname(anyString())).thenReturn(Mono.just(false));
        when(userRepository.existsBySocialIdAndProvider(anyString(), anyString())).thenReturn(Mono.just(false));
        when(userRepository.save(any(UserEntity.class))).thenReturn(Mono.just(user));
        when(profileRepository.save(any(ProfileEntity.class))).thenReturn(Mono.just(ProfileEntity.builder().build()));
        when(fcmRepository.save(any(FcmEntity.class))).thenReturn(Mono.just(FcmEntity.builder().build()));
        when(fcmRepository.findByUserIdAndToken(anyLong(), anyString())).thenReturn(Mono.just(new FcmEntity(0L, 1L, "fcm")));

        webTestClient.post()
                .uri("/user/")
                .body(Mono.just(param), UserRegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class);
    }
}