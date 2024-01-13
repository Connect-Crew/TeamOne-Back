package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.compositeservice.auth.application.Auth2TokenValidator;
import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.auth.domain.JwtToken;
import com.connectcrew.teamone.compositeservice.auth.domain.TokenClaim;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.LoginRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.RegisterRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.LoginResponse;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.ProfileResponse;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.RefreshResponse;
import com.connectcrew.teamone.compositeservice.composite.adapter.out.web.ProjectWebAdapter;
import com.connectcrew.teamone.compositeservice.composite.adapter.out.web.UserWebAdapter;
import com.connectcrew.teamone.compositeservice.composite.domain.Profile;
import com.connectcrew.teamone.compositeservice.composite.domain.Register;
import com.connectcrew.teamone.compositeservice.composite.domain.User;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;
import com.connectcrew.teamone.compositeservice.config.TestBeanConfig;
import com.connectcrew.teamone.compositeservice.config.TestSecurityConfig;
import com.connectcrew.teamone.compositeservice.global.enums.Role;
import com.connectcrew.teamone.compositeservice.global.enums.Social;
import com.connectcrew.teamone.compositeservice.global.error.domain.ErrorResponse;
import com.connectcrew.teamone.compositeservice.global.error.exception.NotFoundException;
import com.connectcrew.teamone.compositeservice.global.error.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestBeanConfig.class})
@ExtendWith(RestDocumentationExtension.class)
class UserControllerTest {
    @Autowired
    private Auth2TokenValidator tokenValidator;

    @Autowired
    private UserWebAdapter userWebAdapter;

    @Autowired
    private ProjectWebAdapter projectRequest;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        this.webTestClient = WebTestClient.bindToApplicationContext(this.context)
                .configureClient()
                .baseUrl("http://teamone.kro.kr:9080")
                .filter(WebTestClientRestDocumentation.documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint())
                )
                .build();
    }

    @Test
    void loginTest() {
        User user = new User(0L, "socialId", Social.GOOGLE, "testUser", "test@test.com", Role.USER, LocalDateTime.now(), LocalDateTime.now());
        Profile profile = new Profile(0L, "이름", "profile image url", "소개 글", 36.5, 40, List.of(MemberPart.IOS, MemberPart.AOS), List.of(1L, 2L));

        when(tokenValidator.validate(anyString(), any(Social.class))).thenReturn(Mono.just("socialId"));
        when(userWebAdapter.getUser(anyString(), any(Social.class))).thenReturn(Mono.just(user));
        when(userWebAdapter.getProfile(anyLong())).thenReturn(Mono.just(profile));
        when(userWebAdapter.saveFcm(anyLong(), anyString())).thenReturn(Mono.just(true));
        when(jwtProvider.createToken(anyString(), anyLong(), anyString(), any(Role.class))).thenReturn(new JwtToken("accessToken", LocalDateTime.now(), "refreshToken", LocalDateTime.now()));

        webTestClient.post()
                .uri("/auth/login")
                .bodyValue(new LoginRequest("sampleToken", Social.GOOGLE, "fcm"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .consumeWith(document("auth/login-success",
                        requestFields(
                                fieldWithPath("token").type("String").description("Social 로그인 후 발급받은 토큰"),
                                fieldWithPath("social").type("String").description(String.format("Social 타입 %s", Arrays.toString(Social.values()))),
                                fieldWithPath("fcm").type("String").description("FCM 토큰")
                        ),
                        responseFields(
                                fieldWithPath("id").type("Number").description("사용자 고유 ID"),
                                fieldWithPath("nickname").type("String").description("사용자 닉네임"),
                                fieldWithPath("profile").type("String (Optional)").optional().description("사용자 프로필 사진 URL"),
                                fieldWithPath("introduction").type("String (Optional)").optional().description("사용자 소개"),
                                fieldWithPath("temperature").type("Number").optional().description("사용자 온도"),
                                fieldWithPath("responseRate").type("Number").optional().description("사용자 응답률"),
                                fieldWithPath("parts[]").type("Part[]").optional().description("사용자 직무"),
                                fieldWithPath("parts[].key").type("String").optional().description("사용자 직무 key"),
                                fieldWithPath("parts[].part").type("String").optional().description("사용자 직무"),
                                fieldWithPath("parts[].category").type("String").optional().description("사용자 직무 카테고리"),
                                fieldWithPath("email").type("String (Optional)").optional().description("사용자 이메일"),
                                fieldWithPath("token").type("String").description("Access Token"),
                                fieldWithPath("exp").type("Datetime").description("Access Token 만료 시간"),
                                fieldWithPath("refreshToken").type("String").description("Refresh Token"),
                                fieldWithPath("refreshExp").type("Datetime").description("Refresh Token 만료 시간")
                        )
                ));
    }

    @Test
    void notRegisterLogin() {
        when(tokenValidator.validate(anyString(), any(Social.class))).thenReturn(Mono.just("socialId"));
        when(userWebAdapter.getUser(anyString(), any(Social.class))).thenReturn(Mono.error(new NotFoundException("사용자를 찾을 수 없습니다.")));
        when(userWebAdapter.saveFcm(anyLong(), anyString())).thenReturn(Mono.just(true));
        when(jwtProvider.createToken(anyString(), anyLong(), anyString(), any(Role.class))).thenReturn(new JwtToken("accessToken", LocalDateTime.now(), "refreshToken", LocalDateTime.now()));

        webTestClient.post()
                .uri("/auth/login")
                .bodyValue(new LoginRequest("sampleToken", Social.GOOGLE, "fcm"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .consumeWith(document("auth/login-failure-not-register",
                        responseFields(
                                fieldWithPath("path").type("String").description("요청 경로"),
                                fieldWithPath("status").type("Integer").description("응답 코드"),
                                fieldWithPath("error").type("String").description("에러 유형"),
                                fieldWithPath("message").type("String").description("실패 메시지"),
                                fieldWithPath("timestamp").type("Datetime").description("응답 시간")
                        )
                ))
                .consumeWith(response -> {
                    ErrorResponse result = response.getResponseBody();

                    assert result != null;
                    assertThat(result.getPath()).isEqualTo("/auth/login");
                    assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
                    assertThat(result.getError()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
                    assertThat(result.getMessage()).isEqualTo("사용자를 찾을 수 없습니다.");
                });
    }

    @Test
    void invalidLoginTest() {
        when(tokenValidator.validate(anyString(), any(Social.class))).thenReturn(Mono.error(new UnauthorizedException("Invalid Token")));

        webTestClient.post()
                .uri("/auth/login")
                .bodyValue(new LoginRequest("sampleToken", Social.GOOGLE, "fcm"))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResponse.class)
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
                    ErrorResponse result = response.getResponseBody();

                    assert result != null;
                    assertThat(result.getPath()).isEqualTo("/auth/login");
                    assertThat(result.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
                    assertThat(result.getError()).isEqualTo(HttpStatus.UNAUTHORIZED.getReasonPhrase());
                    assertThat(result.getMessage()).isEqualTo("유효하지 않은 Token 입니다.");
                });
    }

    @Test
    void registerTest() {
        User user = new User(0L, "socialId", Social.GOOGLE, "testUser", "test@test.com", Role.USER, LocalDateTime.now(), LocalDateTime.now());
        Profile profile = new Profile(0L, "이름", "profile image url", "소개 글", 36.5, 40, List.of(MemberPart.IOS, MemberPart.AOS), List.of(1L, 2L));

        when(tokenValidator.validate(anyString(), any(Social.class))).thenReturn(Mono.just("socialId"));
        when(userWebAdapter.save(any(Register.class))).thenReturn(Mono.just(user));
        when(userWebAdapter.getProfile(anyLong())).thenReturn(Mono.just(profile));
        when(jwtProvider.createToken(anyString(), anyLong(), anyString(), any(Role.class))).thenReturn(new JwtToken("accessToken", LocalDateTime.now(), "refreshToken", LocalDateTime.now()));

        webTestClient.post()
                .uri("/auth/register")
                .bodyValue(new RegisterRequest("sampleToken", Social.GOOGLE, "testUser", "testNick", null, "test@gmail.com", true, true, "fcm"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .consumeWith(document("auth/register-success",
                        requestFields(
                                fieldWithPath("token").type("String").description("Social 로그인 후 발급받은 토큰"),
                                fieldWithPath("social").type("String").description(String.format("Social 타입 %s", Arrays.toString(Social.values()))),
                                fieldWithPath("username").type("string").description("사용자 이름"),
                                fieldWithPath("nickname").type("String").description("사용자 닉네임"),
                                fieldWithPath("profile").type("String (Optional)").optional().description("사용자 프로필 사진 URL"),
                                fieldWithPath("email").type("String (Optional)").optional().description("사용자 이메일"),
                                fieldWithPath("termsAgreement").type("Boolean").description("이용약관 동의 여부"),
                                fieldWithPath("privacyAgreement").type("Boolean").description("개인정보 처리방침 동의 여부"),
                                fieldWithPath("fcm").type("String").description("FCM 토큰")
                        ),
                        responseFields(
                                fieldWithPath("id").type("Number").description("사용자 고유 ID"),
                                fieldWithPath("nickname").type("String").description("사용자 닉네임"),
                                fieldWithPath("profile").type("String (Optional)").optional().description("사용자 프로필 사진 URL"),
                                fieldWithPath("introduction").type("String (Optional)").optional().description("사용자 소개"),
                                fieldWithPath("temperature").type("Number").optional().description("사용자 온도"),
                                fieldWithPath("responseRate").type("Number").optional().description("사용자 응답률"),
                                fieldWithPath("parts[]").type("Part[]").optional().description("사용자 직무"),
                                fieldWithPath("parts[].key").type("String").optional().description("사용자 직무 key"),
                                fieldWithPath("parts[].part").type("String").optional().description("사용자 직무"),
                                fieldWithPath("parts[].category").type("String").optional().description("사용자 직무 카테고리"),
                                fieldWithPath("email").type("String (Optional)").optional().description("사용자 이메일"),
                                fieldWithPath("token").type("String").description("Access Token"),
                                fieldWithPath("exp").type("Datetime").description("Access Token 만료 시간"),
                                fieldWithPath("refreshToken").type("String").description("Refresh Token"),
                                fieldWithPath("refreshExp").type("Datetime").description("Refresh Token 만료 시간")
                        )
                ));
    }

    @Test
    void UnauthorizedRegisterTest() {
        when(tokenValidator.validate(anyString(), any(Social.class))).thenReturn(Mono.error(new UnauthorizedException("Invalid Token")));

        webTestClient.post()
                .uri("/auth/register")
                .bodyValue(new RegisterRequest("sampleToken", Social.GOOGLE, "testUser", "testNick", null, "test@gmail.com", true, true, "fcm"))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResponse.class)
                .consumeWith(document("auth/unauthorized-register-failure",
                        responseFields(
                                fieldWithPath("path").type("String").description("요청 경로"),
                                fieldWithPath("status").type("Integer").description("응답 코드"),
                                fieldWithPath("error").type("String").description("에러 유형"),
                                fieldWithPath("message").type("String").description("실패 메시지"),
                                fieldWithPath("timestamp").type("Datetime").description("응답 시간")
                        )
                ))
                .consumeWith(response -> {
                    ErrorResponse result = response.getResponseBody();

                    assert result != null;
                    assertThat(result.getPath()).isEqualTo("/auth/register");
                    assertThat(result.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
                    assertThat(result.getError()).isEqualTo(HttpStatus.UNAUTHORIZED.getReasonPhrase());
                    assertThat(result.getMessage()).isEqualTo("유효하지 않은 Token 입니다.");
                });
    }

    @Test
    void invalidRegisterTest() {
        when(tokenValidator.validate(anyString(), any(Social.class))).thenReturn(Mono.just("socialId"));
        when(userWebAdapter.save(any(Register.class))).thenReturn(Mono.error(new IllegalArgumentException("공백과 특수문자는 들어갈 수 없어요.")));

        webTestClient.post()
                .uri("/auth/register")
                .bodyValue(new RegisterRequest("sampleToken", Social.GOOGLE, "testUser", "testNick", null, "test@gmail.com", true, true, "fcm"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .consumeWith(document("auth/register-failure",
                        responseFields(
                                fieldWithPath("path").type("String").description("요청 경로"),
                                fieldWithPath("status").type("Integer").description("응답 코드"),
                                fieldWithPath("error").type("String").description("에러 유형"),
                                fieldWithPath("message").type("String").description("실패 메시지"),
                                fieldWithPath("timestamp").type("Datetime").description("응답 시간")
                        )
                ))
                .consumeWith(response -> {
                    ErrorResponse result = response.getResponseBody();

                    assert result != null;
                    assertThat(result.getPath()).isEqualTo("/auth/register");
                    assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                    assertThat(result.getError()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
                    assertThat(result.getMessage()).isEqualTo("공백과 특수문자는 들어갈 수 없어요.");
                });
    }

    @Test
    void refreshTest() {
        when(jwtProvider.getTokenClaim(anyString())).thenReturn(new TokenClaim("socialId", Role.USER, 0L, "nickname"));
        when(jwtProvider.createToken(anyString(), anyLong(), anyString(), any(Role.class))).thenReturn(new JwtToken("accessToken", LocalDateTime.now(), "refreshToken", LocalDateTime.now()));

        webTestClient.post()
                .uri("/auth/refresh")
                .header(JwtProvider.AUTH_HEADER, "Bearer refreshToken")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RefreshResponse.class)
                .consumeWith(document("auth/refresh",
                        requestHeaders(
                                headerWithName(JwtProvider.AUTH_HEADER).description("Bearer RefreshToken")
                        ),
                        responseFields(
                                fieldWithPath("token").type("String").description("새로 발급된 Access Token"),
                                fieldWithPath("exp").type("Datetime").description("Access Token 만료 시간"),
                                fieldWithPath("refresh").type("String").description("새로 발급된 Refresh Token"),
                                fieldWithPath("refreshExp").type("Datetime").description("Refresh Token 만료 시간")
                        )
                ));
    }

    @Test
    void getMyProfile() {
        Profile profile = new Profile(
                0L,
                "이름",
                "profile image url",
                "소개 글",
                36.5,
                40,
                List.of(MemberPart.IOS, MemberPart.AOS),
                List.of(1L, 2L)
        );
        when(jwtProvider.getTokenClaim(anyString())).thenReturn(new TokenClaim("socialId", Role.USER, 0L, "nickname"));
        when(userWebAdapter.getProfile(anyLong())).thenReturn(Mono.just(profile));
        when(projectRequest.findProjectThumbnail(anyLong())).thenReturn(Mono.just(String.format("%s.jpg", UUID.randomUUID())));

        webTestClient.get()
                .uri("/user/myprofile")
                .header(JwtProvider.AUTH_HEADER, "Bearer myToken")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProfileResponse.class)
                .consumeWith(document("user/getMyProfile",
                        requestHeaders(
                                headerWithName(JwtProvider.AUTH_HEADER).description(JwtProvider.BEARER_PREFIX + "Access Token")
                        ),
                        responseFields(
                                fieldWithPath("id").type("Number").description("사용자 ID"),
                                fieldWithPath("nickname").type("String").description("닉네임"),
                                fieldWithPath("profile").type("String (Optional)").description("프로필 이미지"),
                                fieldWithPath("introduction").type("String").description("소개글"),
                                fieldWithPath("temperature").type("String").description("온도"),
                                fieldWithPath("responseRate").type("Number").description("응답률"),
                                fieldWithPath("parts[]").type("Part[]").optional().description("사용자 직무"),
                                fieldWithPath("parts[].key").type("String").optional().description("사용자 직무 key"),
                                fieldWithPath("parts[].part").type("String").optional().description("사용자 직무"),
                                fieldWithPath("parts[].category").type("String").optional().description("사용자 직무 카테고리"),
                                fieldWithPath("representProjects[]").type("RepresentProject[]").description("대표 프로젝트"),
                                fieldWithPath("representProjects[].id").type("Number").description("대표 프로젝트 ID"),
                                fieldWithPath("representProjects[].thumbnail").type("String").description("대표 프로젝트 썸네일")
                        )
                ));
    }
}