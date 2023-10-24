package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.compositeservice.auth.Auth2User;
import com.connectcrew.teamone.compositeservice.auth.TokenGenerator;
import com.connectcrew.teamone.compositeservice.auth.TokenResolver;
import com.connectcrew.teamone.compositeservice.exception.UnauthorizedException;
import com.connectcrew.teamone.compositeservice.request.UserRequest;
import com.connectcrew.teamone.compositeservice.resposne.LoginResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest
@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
class AuthControllerTest {
    @MockBean
    private TokenResolver tokenResolver;

    @MockBean
    private UserRequest userRequest;
    @MockBean
    private TokenGenerator tokenGenerator;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        this.webTestClient = WebTestClient.bindToApplicationContext(this.context)
                .configureClient()
                .baseUrl("http://teamone.kro.kr:9080")
                .filter(WebTestClientRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void firstLoginTest() {
        Auth2User authUser = new Auth2User("socialId", "test@test.com", "testUser", "testProfile", Social.GOOGLE);
        User user = new User(0L, "socialId", Social.GOOGLE, "testUser", "testUser", null, "test@test.com", Role.USER, LocalDateTime.now().toString(), LocalDateTime.now().toString());

        when(tokenResolver.resolve(anyString(), any(Social.class))).thenReturn(Mono.just(authUser));
        when(userRequest.getUser(anyString(), any(Social.class))).thenReturn(Mono.empty());
        when(userRequest.saveUser(any(UserInputParam.class))).thenReturn(Mono.just(user));
        when(tokenGenerator.createToken(anyString(), any(Role.class))).thenReturn("accessToken");
        when(tokenGenerator.createRefreshToken(anyString(), any(Role.class))).thenReturn("refreshToken");

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/login")
                        .queryParam("token", "sampleToken")
                        .queryParam("social", Social.GOOGLE)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResult.class)
                .consumeWith(document("auth/login-success",
                        queryParameters(
                                parameterWithName("token").description("Social 로그인 후 발급받은 토큰"),
                                parameterWithName("social").description(String.format("Social 타입 %s", Arrays.toString(Social.values())))
                        ),
                        responseFields(
                                fieldWithPath("token").type("String").description("Access Token"),
                                fieldWithPath("refreshToken").type("String").description("Refresh Token"),
                                fieldWithPath("nickname").type("String").description("사용자 닉네임"),
                                fieldWithPath("profile").type("String").description("사용자 프로필 사진 URL"),
                                fieldWithPath("email").type("String").description("사용자 이메일"),
                                fieldWithPath("isNewUser").type("boolean").description("신규 유저 여부")
                        )
                ))
                .consumeWith(response -> {
                    LoginResult result = response.getResponseBody();
                    assert result != null;
                    assertThat(result.token()).isEqualTo("accessToken");
                    assertThat(result.refreshToken()).isEqualTo("refreshToken");
                    assertThat(result.nickname()).isEqualTo(user.nickname());
                    assertThat(result.profile()).isEqualTo(user.profile());
                    assertThat(result.email()).isEqualTo(user.email());
                    assertThat(result.isNewUser()).isTrue();
                });
    }

    @Test
    void loginTest() {
        Auth2User authUser = new Auth2User("socialId", "test@test.com", "testUser", "testProfile", Social.GOOGLE);
        User user = new User(0L, "socialId", Social.GOOGLE, "testUser", "testUser", null, "test@test.com", Role.USER, LocalDateTime.now().toString(), LocalDateTime.now().toString());

        when(tokenResolver.resolve(anyString(), any(Social.class))).thenReturn(Mono.just(authUser));
        when(userRequest.getUser(anyString(), any(Social.class))).thenReturn(Mono.just(user));
        when(userRequest.saveUser(any(UserInputParam.class))).thenReturn(Mono.just(user));
        when(tokenGenerator.createToken(anyString(), any(Role.class))).thenReturn("accessToken");
        when(tokenGenerator.createRefreshToken(anyString(), any(Role.class))).thenReturn("refreshToken");

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/auth/login")
                        .queryParam("token", "testToken")
                        .queryParam("social", Social.GOOGLE)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResult.class)
                .consumeWith(response -> {
                    LoginResult result = response.getResponseBody();
                    assert result != null;
                    assertThat(result.token()).isEqualTo("accessToken");
                    assertThat(result.refreshToken()).isEqualTo("refreshToken");
                    assertThat(result.nickname()).isEqualTo(user.nickname());
                    assertThat(result.profile()).isEqualTo(user.profile());
                    assertThat(result.email()).isEqualTo(user.email());
                    assertThat(result.isNewUser()).isFalse();
                });
    }

    @Test
    void invalidLoginTest() {
        when(tokenResolver.resolve(anyString(), any(Social.class))).thenReturn(Mono.error(new UnauthorizedException("Invalid Token")));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/auth/login")
                        .queryParam("token", "testToken")
                        .queryParam("social", Social.GOOGLE)
                        .build())
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorInfo.class)
                .consumeWith(document("auth/login-failure",
                        responseFields(
                                fieldWithPath("path").type("String").description("요청 경로"),
                                fieldWithPath("status").type("Integer").description("응답 코드"),
                                fieldWithPath("error").type("String").description("에러 유형"),
                                fieldWithPath("message").type("String").description("실패 메시지"),
                                fieldWithPath("timestamp").type("Datetime").description("응답 시간")
                        )
                ))
                .consumeWith(response -> {
                    ErrorInfo result = response.getResponseBody();

                    assert result != null;
                    assertThat(result.getPath()).isEqualTo("/auth/login");
                    assertThat(result.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
                    assertThat(result.getError()).isEqualTo(HttpStatus.UNAUTHORIZED.getReasonPhrase());
                    assertThat(result.getMessage()).isEqualTo("유효하지 않은 Token 입니다.");
                });

    }
}