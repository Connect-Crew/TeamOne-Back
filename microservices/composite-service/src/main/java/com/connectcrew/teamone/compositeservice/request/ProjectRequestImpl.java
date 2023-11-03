package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.api.project.ProjectDetail;
import com.connectcrew.teamone.api.project.ProjectFilterOption;
import com.connectcrew.teamone.api.project.ProjectItem;
import com.connectcrew.teamone.api.project.RecruitStatus;
import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.compositeservice.exception.WebClientExceptionHandler;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProjectRequestImpl implements ProjectRequest {

    public final String host;

    private final WebClient webClient;

    private final WebClientExceptionHandler exHandler;

    public ProjectRequestImpl(String host, WebClient webClient) {
        this.host = host;
        this.webClient = webClient;
        this.exHandler = new WebClientExceptionHandler();
    }

    @Override
    public Flux<ProjectItem> getProjectList(int lastId, int size, ProjectFilterOption option) {
        return Flux.fromIterable(List.of(
                        new ProjectItem(
                                0L,
                                "[서울] 강아지 의료 플랫폼 기획",
                                null,
                                Region.SEOUL,
                                false,
                                Career.SEEKER,
                                Career.YEAR_1,
                                LocalDateTime.now().minusMinutes(5),
                                LocalDate.now().plusDays(2),
                                LocalDate.now().plusDays(5),
                                ProjectState.RECRUITING,
                                49,
                                List.of(ProjectCategory.IT),
                                ProjectGoal.PORTFOLIO,
                                List.of(
                                        new RecruitStatus(
                                                MemberPart.PLANNER,
                                                "프로토타입 기획자를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.DESIGNER,
                                                "프로토타입 디자이너를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.ANDROID,
                                                "코틀린을 이용한 안드로이드 앱 개발자를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.IOS,
                                                "Swift를 이용한 iOS 앱 개발자를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.BACKEND,
                                                "Spring 백엔드 개발자를 모집합니다.",
                                                1,
                                                2
                                        )
                                )
                        ),
                        new ProjectItem(
                                1L,
                                "배달비 없는 배달앱 - 함께 하실 크루원을 모집합니다!",
                                null,
                                Region.NONE,
                                true,
                                Career.NONE,
                                Career.NONE,
                                LocalDateTime.now().minusMinutes(10),
                                null,
                                null,
                                ProjectState.PROCEEDING,
                                40,
                                List.of(ProjectCategory.APP),
                                ProjectGoal.STARTUP,
                                List.of(
                                        new RecruitStatus(
                                                MemberPart.PLANNER,
                                                "프로토타입 기획자를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.DESIGNER,
                                                "프로토타입 디자이너를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.ANDROID,
                                                "코틀린을 이용한 안드로이드 앱 개발자를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.IOS,
                                                "Swift를 이용한 iOS 앱 개발자를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.BACKEND,
                                                "Spring 백엔드 개발자를 모집합니다.",
                                                1,
                                                2
                                        )
                                )
                        ),
                        new ProjectItem(
                                2L,
                                "이루다",
                                null,
                                Region.BUSAN,
                                false,
                                Career.NONE,
                                Career.NONE,
                                LocalDateTime.now().minusMinutes(15),
                                LocalDate.now().plusDays(2),
                                LocalDate.now().plusDays(15),
                                ProjectState.RECRUITING,
                                49,
                                List.of(ProjectCategory.AI),
                                ProjectGoal.STARTUP,
                                List.of(
                                        new RecruitStatus(
                                                MemberPart.PLANNER,
                                                "프로토타입 기획자를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.DESIGNER,
                                                "프로토타입 디자이너를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.ANDROID,
                                                "코틀린을 이용한 안드로이드 앱 개발자를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.IOS,
                                                "Swift를 이용한 iOS 앱 개발자를 모집합니다.",
                                                1,
                                                2
                                        ),
                                        new RecruitStatus(
                                                MemberPart.BACKEND,
                                                "Spring 백엔드 개발자를 모집합니다.",
                                                1,
                                                2
                                        )
                                )
                        )
                )
        );
    }

    @Override
    public Mono<ProjectDetail> getProjectDetail(Long projectId) {
        return webClient.get()
                .uri(String.format("%s/", host))
                .retrieve()
                .bodyToMono(ProjectDetail.class)
                .onErrorResume(exHandler::handleException);
    }
}
