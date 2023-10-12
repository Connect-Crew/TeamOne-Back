package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.compositeservice.model.SetupTestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest
@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
class SetupTestControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        webTestClient = WebTestClient.bindToController(new SetupTestController())
                .configureClient()
                .filter(WebTestClientRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void restDocsTest() {
        String id = "TestId";
        String input = "TestInput";
        SetupTestResponse response = new SetupTestResponse(id, "Hello, world! " + input);

        webTestClient.get()
                .uri(String.format("/setup-test?id=%s&input=%s", id, input))
                // .attribute("org.springframework.restdocs.urlTemplate", "/composite/{postId}") // pathParameter의 경우
                .exchange()
                .expectStatus().isOk()
                .expectBody(SetupTestResponse.class).isEqualTo(response)
                .consumeWith(document("setup-test",
                        // Body의 경우
//                        requestFields(
//                                fieldWithPath("id").description("The ID of the test to set up"),
//                                fieldWithPath("input").description("The input to the test")
//                        ),
                        // pathParameter의 경우
//                        pathParameters(
//                                RequestDocumentation.parameterWithName("postId").description("post id")
//                        ),
                        queryParameters( // Document request parameters
                                parameterWithName("id").description("The ID of the test to set up"),
                                parameterWithName("input").description("The input to the test")
                        ),
                        // Response의 경우
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("The ID of the test"),
                                fieldWithPath("contents").type(JsonFieldType.STRING).description("The contents of the test")
                        )
                ));
    }
}