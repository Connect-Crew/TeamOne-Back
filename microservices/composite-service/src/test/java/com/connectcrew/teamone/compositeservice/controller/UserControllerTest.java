package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.api.user.profile.Profile;
import com.connectcrew.teamone.compositeservice.auth.JwtProvider;
import com.connectcrew.teamone.compositeservice.config.TestSecurityConfig;
import com.connectcrew.teamone.compositeservice.request.ProjectRequest;
import com.connectcrew.teamone.compositeservice.request.UserRequestImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private UserRequestImpl userRequest;

    @MockBean
    private ProjectRequest projectRequest;


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
    void getMyProfile() {
        Profile profile = new Profile(
                0L,
                "이름",
                "profile image url",
                "소개 글",
                36.5,
                40,
                List.of(MemberPart.IOS.name(), MemberPart.AOS.name())
        );
        when(jwtProvider.getId(anyString())).thenReturn(1L);
        when(userRequest.getProfile(anyLong())).thenReturn(Mono.just(profile));

        webTestClient.get()
                .uri("/user/myprofile")
                .header(JwtProvider.AUTH_HEADER, "Bearer myToken")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Profile.class)
                .consumeWith(document("user/getMyProfile",
                        requestHeaders(
                                headerWithName(JwtProvider.AUTH_HEADER).description(JwtProvider.BEARER_PREFIX + "Access Token")
                        ),
                        responseFields(
                                fieldWithPath("id").type("Number").description("사용자 ID"),
                                fieldWithPath("nickname").type("String").description("닉네임"),
                                fieldWithPath("profile").type("String").description("프로필 이미지"),
                                fieldWithPath("introduction").type("String").description("소개글"),
                                fieldWithPath("temperature").type("String").description("온도"),
                                fieldWithPath("responseRate").type("Number").description("응답률"),
                                fieldWithPath("parts[]").type("String[]").description("직무 분야")
                        )
                ));
    }
}