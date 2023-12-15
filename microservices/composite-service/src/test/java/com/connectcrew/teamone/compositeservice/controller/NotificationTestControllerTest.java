package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.compositeservice.config.TestSecurityConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;

@WebFluxTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class NotificationTestControllerTest {
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Autowired
//    private ApplicationContext context;
//
//    @MockBean
//    private JwtProvider jwtProvider;
//
//    @MockBean
//    private UserWebAdapter userRequest;
//
//    @MockBean
//    private ProjectRequest projectRequest;
//
//    @BeforeEach
//    void setup(RestDocumentationContextProvider restDocumentation) {
//        this.webTestClient = WebTestClient.bindToApplicationContext(this.context)
//                .configureClient()
//                .baseUrl("http://teamone.kro.kr:9080")
//                .filter(WebTestClientRestDocumentation.documentationConfiguration(restDocumentation)
//                        .operationPreprocessors()
//                        .withRequestDefaults(prettyPrint())
//                        .withResponseDefaults(prettyPrint())
//                )
//                .build();
//    }
//
//    @Test
//    void sendNotification() {
//        when(userRequest.sendNotification(any(FcmNotification.class))).thenReturn(Mono.just(true));
//
//        webTestClient.post()
//                .uri("/notification")
//                .header(JwtProvider.AUTH_HEADER, "Bearer myToken")
//                .bodyValue(new NotificationTestRequest("title", "body"))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(BooleanValueRes.class)
//                .consumeWith(document("notification/test-notification",
//                        requestHeaders(
//                                headerWithName(JwtProvider.AUTH_HEADER).description(JwtProvider.BEARER_PREFIX + "Access Token")
//                        ),
//                        requestFields(
//                                fieldWithPath("title").type("String").description("Notification Title"),
//                                fieldWithPath("body").type("String").description("Notification Body")
//                        ),
//                        responseFields(
//                                fieldWithPath("value").type("Boolean").description("Notification Result")
//                        )
//                ));
//    }

}