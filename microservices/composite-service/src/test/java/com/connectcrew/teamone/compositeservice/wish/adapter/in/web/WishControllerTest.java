package com.connectcrew.teamone.compositeservice.wish.adapter.in.web;

import com.connectcrew.teamone.api.userservice.user.Role;
import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.auth.domain.TokenClaim;
import com.connectcrew.teamone.compositeservice.config.TestBeanConfig;
import com.connectcrew.teamone.compositeservice.config.TestSecurityConfig;
import com.connectcrew.teamone.compositeservice.wish.adapter.in.web.request.WishRequest;
import com.connectcrew.teamone.compositeservice.wish.adapter.in.web.response.WishResponse;
import com.connectcrew.teamone.compositeservice.wish.adapter.out.event.WishEventAdapter;
import com.connectcrew.teamone.compositeservice.wish.domain.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
class WishControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private WishEventAdapter wishEventAdapter;

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
    void sendWishTest() {
        when(jwtProvider.getTokenClaim(anyString())).thenReturn(new TokenClaim("socialId", Role.USER, 0L, "nickname"));
        when(wishEventAdapter.send(any())).thenReturn(new Wish(0L, "wish message", LocalDateTime.now()));

        webTestClient.post()
                .uri("/wish")
                .header(JwtProvider.AUTH_HEADER, "Bearer myToken")
                .bodyValue(new WishRequest("wish message"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(WishResponse.class)
                .consumeWith(document("wish/sendWish",
                        requestHeaders(
                                headerWithName(JwtProvider.AUTH_HEADER).description(JwtProvider.BEARER_PREFIX + "Access Token")
                        ),
                        requestFields(
                                fieldWithPath("message").type("String").description("바랍니다 메시지")
                        ),
                        responseFields(
                                fieldWithPath("message").type("String").description("바랍니다 메시지")
                        )
                ));
    }
}