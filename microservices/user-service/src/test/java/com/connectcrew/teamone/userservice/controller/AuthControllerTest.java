package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.userservice.entity.UserEntity;
import com.connectcrew.teamone.userservice.repository.UserRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private UserRepository repository;

    @Test
    void find() {
        UserEntity user = UserEntity.builder()
                .id(10L)
                .provider(Social.GOOGLE.name())
                .socialId("123456789")
                .username("testName")
                .nickname("testNick")
                .profile("testProfile")
                .email("email@google.com")
                .role(Role.USER.name())
                .termsAgreement(true)
                .privacyAgreement(true)
                .communityPolicyAgreement(true)
                .adNotificationAgreement(true)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        when(repository.findBySocialIdAndProvider(anyString(), anyString())).thenReturn(Mono.just(user));

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
        when(repository.findBySocialIdAndProvider(anyString(), anyString())).thenReturn(Mono.empty());

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
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", false, true, true, true), "서비스 이용약관에 동의해주세요."),
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", true, false, true, true), "개인정보 처리방침에 동의해주세요."),
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", true, true, false, true), "커뮤니티 정책에 동의해주세요."),
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "T", null, "Test@Test.com", true, true, true, true), "최소 2글자 이상 입력해주세요!"),
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "TestNickOver10Length", null, "Test@Test.com", true, true, true, true), "최대 10글자 이하로 입력해주세요!"),
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "Test Nick", null, "Test@Test.com", true, true, true, true), "공백과 특수문자는 들어갈 수 없어요."),
                Arguments.of(new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "dupNick", null, "Test@Test.com", true, true, true, true), "이미 존재하는 닉네임입니다.")
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
                .nickname("testNick")
                .profile("testProfile")
                .email("email@google.com")
                .role(Role.USER.name())
                .termsAgreement(true)
                .privacyAgreement(true)
                .communityPolicyAgreement(true)
                .adNotificationAgreement(true)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        when(repository.existsByNickname(anyString())).thenReturn(Mono.just(false));
        when(repository.existsByNickname("dupNick")).thenReturn(Mono.just(true));
        when(repository.existsBySocialIdAndProvider(anyString(), anyString())).thenReturn(Mono.just(false));
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(user));

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
        UserInputParam param = new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", true, true, true, true);
        UserEntity user = UserEntity.builder()
                .id(10L)
                .provider(Social.GOOGLE.name())
                .socialId("123456789")
                .username("testName")
                .nickname("testNick")
                .profile("testProfile")
                .email("email@google.com")
                .role(Role.USER.name())
                .termsAgreement(true)
                .privacyAgreement(true)
                .communityPolicyAgreement(true)
                .adNotificationAgreement(true)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        when(repository.existsByNickname(anyString())).thenReturn(Mono.just(false));
        when(repository.existsBySocialIdAndProvider(anyString(), anyString())).thenReturn(Mono.just(true));
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(user));

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
        UserInputParam param = new UserInputParam("TestSocial", Social.GOOGLE, "TestUser", "TestNick", null, "Test@Test.com", true, true, true, true);
        UserEntity user = UserEntity.builder()
                .id(10L)
                .provider(Social.GOOGLE.name())
                .socialId("123456789")
                .username("testName")
                .nickname("testNick")
                .profile("testProfile")
                .email("email@google.com")
                .role(Role.USER.name())
                .termsAgreement(true)
                .privacyAgreement(true)
                .communityPolicyAgreement(true)
                .adNotificationAgreement(true)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        when(repository.existsByNickname(anyString())).thenReturn(Mono.just(false));
        when(repository.existsBySocialIdAndProvider(anyString(), anyString())).thenReturn(Mono.just(false));
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/user/")
                .body(Mono.just(param), UserInputParam.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class);
    }
}