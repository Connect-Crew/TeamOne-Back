package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.user.notification.FcmNotification;
import com.connectcrew.teamone.compositeservice.auth.JwtProvider;
import com.connectcrew.teamone.compositeservice.config.TestSecurityConfig;
import com.connectcrew.teamone.compositeservice.param.NotificationTestParam;
import com.connectcrew.teamone.compositeservice.request.ProjectRequest;
import com.connectcrew.teamone.compositeservice.request.UserRequestImpl;
import com.connectcrew.teamone.compositeservice.resposne.BooleanValueRes;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class NotificationTestControllerTest {
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
    void sendNotification() {
        when(userRequest.sendNotification(any(FcmNotification.class))).thenReturn(Mono.just(true));

        webTestClient.post()
                .uri("/notification")
                .header(JwtProvider.AUTH_HEADER, "Bearer myToken")
                .bodyValue(new NotificationTestParam("title", "body"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(BooleanValueRes.class)
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