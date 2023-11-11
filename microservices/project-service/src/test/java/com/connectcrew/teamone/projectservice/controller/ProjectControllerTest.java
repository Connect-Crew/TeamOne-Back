package com.connectcrew.teamone.projectservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.api.project.ApplyInput;
import com.connectcrew.teamone.api.project.ReportInput;
import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.projectservice.entity.Apply;
import com.connectcrew.teamone.projectservice.entity.Part;
import com.connectcrew.teamone.projectservice.entity.Report;
import com.connectcrew.teamone.projectservice.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private BannerRepository bannerRepository;

    @MockBean
    private PartRepository partRepository;

    @MockBean
    private ApplyRepository applyRepository;

    @MockBean
    private SkillRepository skillRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private ReportRepository reportRepository;

    @MockBean
    private CustomRepository customRepository;

    // apply
    @Test
    void applyTest() {
        Part part = Part.builder()
                .id(1L)
                .project(1L)
                .partCategory("testCategory")
                .part("testPart")
                .comment("testComment")
                .collected(0)
                .targetCollect(2)
                .build();
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(partRepository.findByProjectAndPart(anyLong(), anyString())).thenReturn(Mono.just(part));
        when(memberRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.save(any(Apply.class))).thenReturn(Mono.just(Apply.builder().build()));

        webTestClient.post()
                .uri("/apply")
                .bodyValue(new ApplyInput(
                        1L,
                        1L,
                        MemberPart.ADVERTISEMENT_DESIGNER,
                        "testComment"
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class);
    }

    @Test
    void applyNotfoundProjectTest() {
        Part part = Part.builder()
                .id(1L)
                .project(1L)
                .partCategory("testCategory")
                .part("testPart")
                .comment("testComment")
                .collected(0)
                .targetCollect(2)
                .build();
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(false));
        when(partRepository.findByProjectAndPart(anyLong(), anyString())).thenReturn(Mono.just(part));
        when(memberRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.save(any(Apply.class))).thenReturn(Mono.just(Apply.builder().build()));

        webTestClient.post()
                .uri("/apply")
                .bodyValue(new ApplyInput(
                        1L,
                        1L,
                        MemberPart.ADVERTISEMENT_DESIGNER,
                        "testComment"
                ))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorInfo.class);
    }

    @Test
    void applyNotfoundPartTest() {
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(partRepository.findByProjectAndPart(anyLong(), anyString())).thenReturn(Mono.empty());
        when(memberRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.save(any(Apply.class))).thenReturn(Mono.just(Apply.builder().build()));

        webTestClient.post()
                .uri("/apply")
                .bodyValue(new ApplyInput(
                        1L,
                        1L,
                        MemberPart.ADVERTISEMENT_DESIGNER,
                        "testComment"
                ))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorInfo.class);
    }

    @Test
    void applyAlreadyCollectedTest() {
        Part part = Part.builder()
                .id(1L)
                .project(1L)
                .partCategory("testCategory")
                .part("testPart")
                .comment("testComment")
                .collected(2)
                .targetCollect(2)
                .build();
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(partRepository.findByProjectAndPart(anyLong(), anyString())).thenReturn(Mono.just(part));
        when(memberRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.save(any(Apply.class))).thenReturn(Mono.just(Apply.builder().build()));

        webTestClient.post()
                .uri("/apply")
                .bodyValue(new ApplyInput(
                        1L,
                        1L,
                        MemberPart.ADVERTISEMENT_DESIGNER,
                        "testComment"
                ))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class);
    }

    @Test
    void applyAlreadyMemberTest() {
        Part part = Part.builder()
                .id(1L)
                .project(1L)
                .partCategory("testCategory")
                .part("testPart")
                .comment("testComment")
                .collected(0)
                .targetCollect(2)
                .build();
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(partRepository.findByProjectAndPart(anyLong(), anyString())).thenReturn(Mono.just(part));
        when(memberRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(true));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.save(any(Apply.class))).thenReturn(Mono.just(Apply.builder().build()));

        webTestClient.post()
                .uri("/apply")
                .bodyValue(new ApplyInput(
                        1L,
                        1L,
                        MemberPart.ADVERTISEMENT_DESIGNER,
                        "testComment"
                ))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class);
    }

    @Test
    void applyAlreadyApplyTest() {
        Part part = Part.builder()
                .id(1L)
                .project(1L)
                .partCategory("testCategory")
                .part("testPart")
                .comment("testComment")
                .collected(0)
                .targetCollect(2)
                .build();
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(partRepository.findByProjectAndPart(anyLong(), anyString())).thenReturn(Mono.just(part));
        when(memberRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(true));
        when(applyRepository.save(any(Apply.class))).thenReturn(Mono.just(Apply.builder().build()));

        webTestClient.post()
                .uri("/apply")
                .bodyValue(new ApplyInput(
                        1L,
                        1L,
                        MemberPart.ADVERTISEMENT_DESIGNER,
                        "testComment"
                ))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class);
    }

    // report
    @Test
    void reportTest() {
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(reportRepository.existsByProjectAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(reportRepository.save(any(Report.class))).thenReturn(Mono.just(Report.builder().build()));

        webTestClient.post()
                .uri("/report")
                .bodyValue(new ReportInput(
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
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(false));
        when(reportRepository.existsByProjectAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(reportRepository.save(any(Report.class))).thenReturn(Mono.just(Report.builder().build()));

        webTestClient.post()
                .uri("/report")
                .bodyValue(new ReportInput(
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
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(reportRepository.existsByProjectAndUser(anyLong(), anyLong())).thenReturn(Mono.just(true));
        when(reportRepository.save(any(Report.class))).thenReturn(Mono.just(Report.builder().build()));

        webTestClient.post()
                .uri("/report")
                .bodyValue(new ReportInput(
                        1L,
                        1L,
                        "testComment"
                ))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class);
    }
}