package com.connectcrew.teamone.projectservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.api.projectservice.project.ReportApiRequest;
import com.connectcrew.teamone.projectservice.config.TestConfig;
import com.connectcrew.teamone.projectservice.project.adapter.in.web.ProjectController;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.ProjectEntity;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.ReportEntity;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository.ProjectRepository;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
@WebFluxTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ReportRepository reportRepository;

    // report
    @Test
    void reportTest() {
        ProjectEntity project = ProjectEntity.builder()
                .title("testTitle")
                .build();

        when(projectRepository.findById(anyLong())).thenReturn(Mono.just(project));
        when(reportRepository.existsByProjectAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(reportRepository.save(any(ReportEntity.class))).thenReturn(Mono.just(ReportEntity.builder().build()));

        webTestClient.post()
                .uri("/project/report")
                .bodyValue(new ReportApiRequest(
                        1L,
                        1L,
                        "testComment"
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class);
    }

    @Test
    void reportNotfoundProjectTest() {
        when(projectRepository.findById(anyLong())).thenReturn(Mono.empty());
        when(reportRepository.existsByProjectAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(reportRepository.save(any(ReportEntity.class))).thenReturn(Mono.just(ReportEntity.builder().build()));

        webTestClient.post()
                .uri("/project/report")
                .bodyValue(new ReportApiRequest(
                        1L,
                        1L,
                        "testComment"
                ))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorInfo.class);
    }

    @Test
    void reportAlreadyReportTest() {
        ProjectEntity project = ProjectEntity.builder()
                .title("testTitle")
                .build();

        when(projectRepository.findById(anyLong())).thenReturn(Mono.just(project));
        when(reportRepository.existsByProjectAndUser(anyLong(), anyLong())).thenReturn(Mono.just(true));
        when(reportRepository.save(any(ReportEntity.class))).thenReturn(Mono.just(ReportEntity.builder().build()));

        webTestClient.post()
                .uri("/project/report")
                .bodyValue(new ReportApiRequest(
                        1L,
                        1L,
                        "testComment"
                ))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class);
    }
}