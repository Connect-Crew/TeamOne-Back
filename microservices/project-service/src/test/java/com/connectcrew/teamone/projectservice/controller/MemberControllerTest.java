package com.connectcrew.teamone.projectservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.projectservice.config.TestConfig;
import com.connectcrew.teamone.projectservice.member.adapter.in.web.MemberController;
import com.connectcrew.teamone.api.projectservice.member.ApplyRequest;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.ApplyEntity;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository.ApplyRepository;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository.MemberRepository;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.PartEntity;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.ProjectEntity;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
@WebFluxTest(MemberController.class)
public class MemberControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private ApplyRepository applyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void applyTest() {
        PartEntity part = PartEntity.builder()
                .id(1L)
                .project(1L)
                .partCategory("testCategory")
                .part(MemberPart.AOS.name())
                .comment("testComment")
                .collected(0)
                .targetCollect(2)
                .build();
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(projectRepository.findById(anyLong())).thenReturn(Mono.just(ProjectEntity.builder().leader(1L).build()));
        when(partRepository.findByProjectAndPart(anyLong(), anyString())).thenReturn(Mono.just(part));
        when(memberRepository.countByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(0));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.save(any(ApplyEntity.class))).thenReturn(Mono.just(ApplyEntity.builder().build()));

        webTestClient.post()
                .uri("/member/apply")
                .bodyValue(new ApplyRequest(
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
        PartEntity part = PartEntity.builder()
                .id(1L)
                .project(1L)
                .partCategory("testCategory")
                .part(MemberPart.AOS.name())
                .comment("testComment")
                .collected(0)
                .targetCollect(2)
                .build();
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(false));
        when(partRepository.findByProjectAndPart(anyLong(), anyString())).thenReturn(Mono.just(part));
        when(memberRepository.countByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(0));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.save(any(ApplyEntity.class))).thenReturn(Mono.just(ApplyEntity.builder().build()));

        webTestClient.post()
                .uri("/member/apply")
                .bodyValue(new ApplyRequest(
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
        when(memberRepository.countByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(0));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.save(any(ApplyEntity.class))).thenReturn(Mono.just(ApplyEntity.builder().build()));

        webTestClient.post()
                .uri("/member/apply")
                .bodyValue(new ApplyRequest(
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
        PartEntity part = PartEntity.builder()
                .id(1L)
                .project(1L)
                .partCategory("testCategory")
                .part(MemberPart.AOS.name())
                .comment("testComment")
                .collected(2)
                .targetCollect(2)
                .build();
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(partRepository.findByProjectAndPart(anyLong(), anyString())).thenReturn(Mono.just(part));
        when(memberRepository.countByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(0));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.save(any(ApplyEntity.class))).thenReturn(Mono.just(ApplyEntity.builder().build()));

        webTestClient.post()
                .uri("/member/apply")
                .bodyValue(new ApplyRequest(
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
        PartEntity part = PartEntity.builder()
                .id(1L)
                .project(1L)
                .partCategory("testCategory")
                .part(MemberPart.AOS.name())
                .comment("testComment")
                .collected(0)
                .targetCollect(2)
                .build();
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(partRepository.findByProjectAndPart(anyLong(), anyString())).thenReturn(Mono.just(part));
        when(memberRepository.countByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(1));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(applyRepository.save(any(ApplyEntity.class))).thenReturn(Mono.just(ApplyEntity.builder().build()));

        webTestClient.post()
                .uri("/member/apply")
                .bodyValue(new ApplyRequest(
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
        PartEntity part = PartEntity.builder()
                .id(1L)
                .project(1L)
                .partCategory("testCategory")
                .part(MemberPart.AOS.name())
                .comment("testComment")
                .collected(0)
                .targetCollect(2)
                .build();
        when(projectRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(partRepository.findByProjectAndPart(anyLong(), anyString())).thenReturn(Mono.just(part));
        when(memberRepository.countByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(0));
        when(applyRepository.existsByPartIdAndUser(anyLong(), anyLong())).thenReturn(Mono.just(true));
        when(applyRepository.save(any(ApplyEntity.class))).thenReturn(Mono.just(ApplyEntity.builder().build()));

        webTestClient.post()
                .uri("/member/apply")
                .bodyValue(new ApplyRequest(
                        1L,
                        1L,
                        MemberPart.ADVERTISEMENT_DESIGNER,
                        "testComment"
                ))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class);
    }

}
