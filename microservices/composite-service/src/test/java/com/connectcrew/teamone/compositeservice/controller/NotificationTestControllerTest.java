package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.auth.domain.TokenClaim;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.NotificationTestRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.SimpleBooleanResponse;
import com.connectcrew.teamone.compositeservice.config.TestBeanConfig;
import com.connectcrew.teamone.compositeservice.config.TestSecurityConfig;
import com.connectcrew.teamone.compositeservice.global.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

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
class NotificationTestControllerTest {
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

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
    void sendNotification() {
        when(jwtProvider.getTokenClaim(anyString())).thenReturn(new TokenClaim("socialId", Role.USER, 0L, "nickname"));
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(null);

        webTestClient.post()
                .uri("/notification")
                .header(JwtProvider.AUTH_HEADER, "Bearer myToken")
                .bodyValue(new NotificationTestRequest("title", "body"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(SimpleBooleanResponse.class)
                .consumeWith(document("notification/test-notification",
                        requestHeaders(
                                headerWithName(JwtProvider.AUTH_HEADER).description(JwtProvider.BEARER_PREFIX + "Access Token")
                        ),
                        requestFields(
                                fieldWithPath("title").type("String").description("Notification Title"),
                                fieldWithPath("body").type("String").description("Notification Body")
                        ),
                        responseFields(
                                fieldWithPath("value").type("Boolean").description("Notification Result")
                        )
                ));
    }

}