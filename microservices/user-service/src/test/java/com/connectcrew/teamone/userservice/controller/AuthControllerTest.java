package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.userservice.entity.FcmEntity;
import com.connectcrew.teamone.userservice.entity.ProfileEntity;
import com.connectcrew.teamone.userservice.entity.UserEntity;
import com.connectcrew.teamone.userservice.repository.*;
import com.google.firebase.messaging.FirebaseMessaging;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(AuthController.class)
class AuthControllerTest {

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

        ProfileEntity profile = ProfileEntity.builder()
                .userId(user.getId())
                .nickname("testNick")
                .profile("testProfile")
                .introduction("testIntroduction")
                .temperature(36.5)
                .recvApply(1)
                .resApply(1)
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
                .expectBody(User.class);
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
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", false, true, "fcm"), "서비스 이용약관에 동의해주세요."),
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", true, false, "fcm"), "개인정보 처리방침에 동의해주세요."),
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "T", null, "Test@Test.com", true, true, "fcm"), "최소 2글자 이상 입력해주세요!"),
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "TestNickOver10Length", null, "Test@Test.com", true, true, "fcm"), "최대 10글자 이하로 입력해주세요!"),
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "Test Nick", null, "Test@Test.com", true, true, "fcm"), "공백과 특수문자는 들어갈 수 없어요."),
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "dupNick", null, "Test@Test.com", true, true, "fcm"), "이미 존재하는 닉네임입니다.")
        );
    }

    @ParameterizedTest
    @MethodSource("registerFailArgs")
    void registerFailTest(UserInputParam param, String expectMessage) {
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
                .body(Mono.just(param), UserInputParam.class)
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
        UserInputParam param = new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", true, true, "fcm");
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
                .body(Mono.just(param), UserInputParam.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class)
                .value(errorInfo -> {
                    assert errorInfo.getMessage().equals("이미 존재하는 사용자입니다.");
                });
    }

    @Test
    void registerTest() {
        UserInputParam param = new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", true, true, "fcm");
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
                .body(Mono.just(param), UserInputParam.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class);
    }
}