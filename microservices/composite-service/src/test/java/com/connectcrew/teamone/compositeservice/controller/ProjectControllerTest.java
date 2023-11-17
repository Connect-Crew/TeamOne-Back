package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.api.project.*;
import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.api.user.favorite.FavoriteType;
import com.connectcrew.teamone.api.user.profile.Profile;
import com.connectcrew.teamone.compositeservice.auth.JwtProvider;
import com.connectcrew.teamone.compositeservice.config.TestSecurityConfig;
import com.connectcrew.teamone.compositeservice.param.ApplyParam;
import com.connectcrew.teamone.compositeservice.param.ProjectFavoriteParam;
import com.connectcrew.teamone.compositeservice.param.ProjectInputParam;
import com.connectcrew.teamone.compositeservice.param.ReportParam;
import com.connectcrew.teamone.compositeservice.request.ProjectRequest;
import com.connectcrew.teamone.compositeservice.request.UserRequestImpl;
import com.connectcrew.teamone.compositeservice.resposne.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class ProjectControllerTest {

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


    private static String BANNER_PATH;

    public ProjectControllerTest(@Value("${resource.banner}") String bannerPath) {
        BANNER_PATH = bannerPath;

        File file = new File(BANNER_PATH);
        if (!file.exists() && file.mkdir()) {
            System.out.println("banner 폴더 생성");
        }
    }

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

    private List<RecruitStatus> initRecruits() {
        return List.of(
                new RecruitStatus(
                        MemberPart.PL_PM_PO,
                        "프로토타입 기획자를 모집합니다.",
                        1,
                        2
                ),
                new RecruitStatus(
                        MemberPart.UI_UX_DESIGNER,
                        "프로토타입 디자이너를 모집합니다.",
                        1,
                        2
                ),
                new RecruitStatus(
                        MemberPart.AOS,
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
        );
    }

    private List<ProjectItem> initItems() {
        return List.of(
                new ProjectItem(
                        0L,
                        "[서울] 강아지 의료 플랫폼 기획",
                        null,
                        Region.SEOUL,
                        false,
                        Career.SEEKER,
                        Career.YEAR_1,
                        LocalDateTime.now().minusMinutes(5),
                        ProjectState.RECRUITING,
                        49,
                        List.of(ProjectCategory.IT),
                        ProjectGoal.PORTFOLIO,
                        initRecruits()
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
                        ProjectState.PROCEEDING,
                        40,
                        List.of(ProjectCategory.APP),
                        ProjectGoal.STARTUP,
                        initRecruits()
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
                        ProjectState.RECRUITING,
                        49,
                        List.of(ProjectCategory.AI),
                        ProjectGoal.STARTUP,
                        initRecruits()
                )
        );
    }

    @Test
    void listTest() {
        List<ProjectItem> items = initItems();
        when(jwtProvider.getId(anyString())).thenReturn(1L);
        when(projectRequest.getProjectList(any(ProjectFilterOption.class))).thenReturn(Flux.fromIterable(items));
        when(userRequest.isFavorite(anyLong(), any(FavoriteType.class), any(List.class))).thenReturn(Mono.just(Map.of(0L, true, 1L, false, 2L, true)));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/project/list")
                        .queryParam("lastId", 10)
                        .queryParam("size", 10)
                        .queryParam("goal", ProjectGoal.STARTUP.name())
                        .queryParam("career", Career.YEAR_1.name())
                        .queryParam("region", Region.SEOUL.name())
                        .queryParam("online", true)
                        .queryParam("part", MemberPart.AOS.name())
                        .queryParam("skills", List.of(SkillType.Jira.name(), SkillType.Github.name()))
                        .queryParam("states", List.of(ProjectState.PROCEEDING.name(), ProjectState.RECRUITING.name()))
                        .queryParam("category", List.of(ProjectCategory.IT.name()))
                        .queryParam("search", "내가 검색하고자 하는 문장")
                        .build()
                )
                .header(JwtProvider.AUTH_HEADER, "Bearer myToken")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<ProjectItemRes>>() {
                })
                .consumeWith(document("project/list",
                        requestHeaders(
                                headerWithName(JwtProvider.AUTH_HEADER).description(JwtProvider.BEARER_PREFIX + "Access Token")
                        ),
                        queryParameters(
                                parameterWithName("lastId").optional().description("기준 프로젝트 ID (Optional) \n해당 ID를 기준으로 과거 프로젝트를 불러옵니다 \n미 입력시 가장 최신 프로젝트를 불러옵니다."),
                                parameterWithName("size").description("프로젝트 개수"),
                                parameterWithName("goal").description("프로젝트 목표 (Optional)"),
                                parameterWithName("career").optional().description("프로젝트 경력 (Optional)"),
                                parameterWithName("region").optional().description("프로젝트 지역 (Optional)"),
                                parameterWithName("online").optional().description("온라인 여부 (Optional)"),
                                parameterWithName("part").optional().description("프로젝트 직무 (Optional)"),
                                parameterWithName("skills").optional().description("프로젝트 기술 (Optional)"),
                                parameterWithName("states").optional().description("프로젝트 상태 (Optional)"),
                                parameterWithName("category").optional().description("프로젝트 카테고리 (Optional)"),
                                parameterWithName("search").optional().description("프로젝트 검색어 (Optional)")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type("Number").description("프로젝트 ID"),
                                fieldWithPath("[].title").type("String").description("프로젝트 제목"),
                                fieldWithPath("[].thumbnail").type("String (Optional)").optional().description("프로젝트 썸네일"),
                                fieldWithPath("[].region").type("String").description("프로젝트 지역"),
                                fieldWithPath("[].online").type("Boolean").description("온라인 여부"),
                                fieldWithPath("[].careerMin").type("String").description("최소 프로젝트 경력"),
                                fieldWithPath("[].careerMax").type("String").description("최대 프로젝트 경력"),
                                fieldWithPath("[].createdAt").type("Datetime").description("프로젝트 생성 날짜"),
                                fieldWithPath("[].state").type("String").description("프로젝트 상태"),
                                fieldWithPath("[].favorite").type("Number").description("프로젝트 좋아요 수"),
                                fieldWithPath("[].myFavorite").type("Boolean").description("내가 좋아요 한 프로젝트 여부"),
                                fieldWithPath("[].category").type("String[]").description("프로젝트 분야"),
                                fieldWithPath("[].goal").type("String").description("프로젝트 목표"),
                                fieldWithPath("[].recruitStatus").type("RecruitStatus[]").description("프로젝트 모집 현황"),
                                fieldWithPath("[].recruitStatus[].category").type("String").description("프로젝트 모집 직무 카테고리"),
                                fieldWithPath("[].recruitStatus[].part").type("String").description("프로젝트 모집 직무"),
                                fieldWithPath("[].recruitStatus[].comment").type("String").description("프로젝트 모집 코멘트"),
                                fieldWithPath("[].recruitStatus[].current").type("Number").description("프로젝트 현재 인원"),
                                fieldWithPath("[].recruitStatus[].max").type("Number").description("프로젝트 최대 인원")
                        )
                ));
    }

    @Test
    void findTest() {
        ProjectDetail project = new ProjectDetail(
                0L,
                "프로젝트 제목",
                List.of("Image URL 1", "Image URL 2"),
                Region.SEOUL,
                true,
                LocalDateTime.now(),
                ProjectState.PROCEEDING,
                Career.SEEKER,
                Career.YEAR_1,
                List.of(ProjectCategory.IT, ProjectCategory.ECOMMERCE),
                ProjectGoal.STARTUP,
                0L,
                "프로젝트 설명",
                14,
                initRecruits(),
                List.of(SkillType.Swift.name(), SkillType.Kotlin.name(), SkillType.Spring.name())
        );

        when(projectRequest.getProjectDetail(anyLong())).thenReturn(Mono.just(project));
        when(userRequest.getProfile(0L)).thenReturn(Mono.just(new Profile(
                0L,
                "이름",
                "profile image url",
                "소개 글",
                36.5,
                40,
                List.of(MemberPart.IOS.name(), MemberPart.AOS.name())
        )));
        when(userRequest.getProfile(1L)).thenReturn(Mono.just(new Profile(
                1L,
                "이름",
                "profile image url",
                "소개 글",
                36.5,
                40,
                List.of(MemberPart.IOS.name(), MemberPart.AOS.name())
        )));
        when(userRequest.getProfile(2L)).thenReturn(Mono.just(new Profile(
                2L,
                "이름",
                "profile image url",
                "소개 글",
                36.5,
                40,
                List.of(MemberPart.IOS.name(), MemberPart.AOS.name())
        )));
        when(userRequest.getProfile(3L)).thenReturn(Mono.just(new Profile(
                3L,
                "이름",
                "profile image url",
                "소개 글",
                36.5,
                40,
                List.of(MemberPart.IOS.name(), MemberPart.AOS.name())
        )));
        when(jwtProvider.getId(anyString())).thenReturn(1L);
        when(userRequest.isFavorite(anyLong(), any(FavoriteType.class), anyLong())).thenReturn(Mono.just(true));

        webTestClient.get()
                .uri("/project/{projectId}", 0L)
                .header(JwtProvider.AUTH_HEADER, "Bearer myToken")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProjectDetailRes.class)
                .consumeWith(document("project/find-success",
                        requestHeaders(
                                headerWithName(JwtProvider.AUTH_HEADER).description(JwtProvider.BEARER_PREFIX + "Access Token")
                        ),
                        pathParameters(
                                parameterWithName("projectId").description("Project Id")
                        ),
                        responseFields(
                                fieldWithPath("id").type("Number").description("프로젝트 ID"),
                                fieldWithPath("title").type("String").description("프로젝트 제목"),
                                fieldWithPath("banners[]").type("String[]").optional().description("프로젝트 베너 이미지"),
                                fieldWithPath("region").type("String").optional().description("프로젝트 지역"),
                                fieldWithPath("online").type("Boolean").description("온라인 여부"),
                                fieldWithPath("careerMin").type("String").optional().description("최소 프로젝트 경력"),
                                fieldWithPath("careerMax").type("String").optional().description("최대 프로젝트 경력"),
                                fieldWithPath("createdAt").type("Datetime").description("프로젝트 생성 날짜"),
                                fieldWithPath("state").type("String").description("프로젝트 상태"),
                                fieldWithPath("introduction").type("String").description("프로젝트 소개"),
                                fieldWithPath("favorite").type("Number").description("프로젝트 좋아요 수"),
                                fieldWithPath("myFavorite").type("Boolean").description("내가 좋아요 한 프로젝트인지 여부"),
                                fieldWithPath("category").type("String[]").description("프로젝트 분야"),
                                fieldWithPath("goal").type("String").description("프로젝트 목표"),
                                fieldWithPath("leader").type("Profile").description("프로젝트 리더 정보"),
                                fieldWithPath("leader.id").type("Number").description("프로젝트 리더 ID"),
                                fieldWithPath("leader.nickname").type("String").description("프로젝트 리더 이름"),
                                fieldWithPath("leader.profile").type("String").description("프로젝트 리더 프로필 이미지"),
                                fieldWithPath("leader.introduction").type("String").description("프로젝트 리더 소개"),
                                fieldWithPath("leader.temperature").type("String").description("프로젝트 리더 온도"),
                                fieldWithPath("leader.responseRate").type("Number").description("프로젝트 리더 응답률"),
                                fieldWithPath("leader.parts[]").type("String[]").description("프로젝트 리더 분야"),
                                fieldWithPath("recruitStatus").type("RecruitStatus[]").description("프로젝트 모집 현황"),
                                fieldWithPath("recruitStatus[].category").type("String").description("프로젝트 모집 직무 카테고리"),
                                fieldWithPath("recruitStatus[].part").type("String").description("프로젝트 모집 직무"),
                                fieldWithPath("recruitStatus[].comment").type("String").description("프로젝트 모집 코멘트"),
                                fieldWithPath("recruitStatus[].current").type("Number").description("프로젝트 현재 인원"),
                                fieldWithPath("recruitStatus[].max").type("Number").description("프로젝트 최대 인원"),
                                fieldWithPath("skills[]").type("String[]").description("프로젝트 스킬 정보")
                        )
                ));
    }

    @Test
    void notFoundTest() {
        when(projectRequest.getProjectDetail(anyLong())).thenReturn(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString())));
        when(jwtProvider.getId(anyString())).thenReturn(1L);

        webTestClient.get()
                .uri("/project/{projectId}", 0L)
                .header(JwtProvider.AUTH_HEADER, "Bearer myToken")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorInfo.class)
                .consumeWith(document("project/find-not-found",
                        responseFields(
                                fieldWithPath("path").type("String").description("요청 경로"),
                                fieldWithPath("status").type("Integer").description("응답 코드"),
                                fieldWithPath("error").type("String").description("에러 유형"),
                                fieldWithPath("message").type("String").description("실패 메시지"),
                                fieldWithPath("timestamp").type("Datetime").description("응답 시간")
                        )
                ));
    }

    @Test
    void createProjectTest() {
        String token = JwtProvider.BEARER_PREFIX + "access token";
        when(jwtProvider.getId(anyString())).thenReturn(0L);
        when(projectRequest.saveProject(any(ProjectInput.class))).thenReturn(Mono.just(0L));

        ProjectInputParam param = new ProjectInputParam(
                "프로젝트 제목",
                Region.SEOUL,
                true,
                ProjectState.RECRUITING,
                Career.SEEKER,
                Career.YEAR_1,
                List.of(MemberPart.PL_PM_PO, MemberPart.UI_UX_DESIGNER),
                List.of(ProjectCategory.IT, ProjectCategory.ECOMMERCE),
                ProjectGoal.STARTUP,
                "프로젝트 설명",
                List.of(
                        new RecruitInput(MemberPart.PL_PM_PO, "코멘트", 2),
                        new RecruitInput(MemberPart.UI_UX_DESIGNER, "코멘트", 2),
                        new RecruitInput(MemberPart.AOS, "코멘트", 2),
                        new RecruitInput(MemberPart.IOS, "코멘트", 2),
                        new RecruitInput(MemberPart.BACKEND, "코멘트", 2)
                ),
                List.of(SkillType.Swift.name(), SkillType.Kotlin.name(), SkillType.Spring.name())
        );

        MultiValueMap<String, Object> multipartData = new LinkedMultiValueMap<>();
        multipartData.add("param", param);
        multipartData.add("banner", new ClassPathResource("banner1.png"));
        multipartData.add("banner", new ClassPathResource("banner2.png"));

        webTestClient.post()
                .uri("/project/")
                .header(JwtProvider.AUTH_HEADER, token)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(multipartData)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LongValueRes.class)
                .consumeWith(document("project/create-success",
                                requestHeaders(
                                        headerWithName(JwtProvider.AUTH_HEADER).description(JwtProvider.BEARER_PREFIX + "Access Token")
                                ),
                                requestParts(
                                        partWithName("banner").optional().description("프로젝트 배너 이미지 최대 3개로 .jpg, .png, .jpeg 확장자만 허용합니다."),
                                        partWithName("param").description("프로젝트 생성 정보")
                                ),
                                responseFields(
                                        fieldWithPath("value").type("Long").description("생성된 프로젝트 ID")
                                )
                        )
                );

        clearBannerForTest();
    }


    @Test
    void createFailureTest() {
        String token = JwtProvider.BEARER_PREFIX + "access token";
        when(jwtProvider.getId(anyString())).thenReturn(0L);
        when(projectRequest.saveProject(any(ProjectInput.class))).thenReturn(Mono.error(new IllegalArgumentException(ProjectExceptionMessage.TITLE_LENGTH_30_UNDER.toString())));

        ProjectInputParam param = new ProjectInputParam(
                "프로젝트 제목",
                Region.SEOUL,
                true,
                ProjectState.RECRUITING,
                Career.SEEKER,
                Career.YEAR_1,
                List.of(MemberPart.PL_PM_PO, MemberPart.UI_UX_DESIGNER),
                List.of(ProjectCategory.IT, ProjectCategory.ECOMMERCE),
                ProjectGoal.STARTUP,
                "프로젝트 설명",
                List.of(
                        new RecruitInput(MemberPart.PL_PM_PO, "코멘트", 2),
                        new RecruitInput(MemberPart.UI_UX_DESIGNER, "코멘트", 2),
                        new RecruitInput(MemberPart.AOS, "코멘트", 2),
                        new RecruitInput(MemberPart.IOS, "코멘트", 2),
                        new RecruitInput(MemberPart.BACKEND, "코멘트", 2)
                ),
                List.of(SkillType.Swift.name(), SkillType.Kotlin.name(), SkillType.Spring.name())
        );

        MultiValueMap<String, Object> multipartData = new LinkedMultiValueMap<>();
        multipartData.add("param", param);
        multipartData.add("banner", new ClassPathResource("banner1.png"));
        multipartData.add("banner", new ClassPathResource("banner2.png"));

        webTestClient.post()
                .uri("/project/")
                .header(JwtProvider.AUTH_HEADER, token)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(multipartData)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class)
                .consumeWith(document("project/create-failure",
                        responseFields(
                                fieldWithPath("path").type("String").description("요청 경로"),
                                fieldWithPath("status").type("Integer").description("응답 코드"),
                                fieldWithPath("error").type("String").description("에러 유형"),
                                fieldWithPath("message").type("String").description("실패 메시지"),
                                fieldWithPath("timestamp").type("Datetime").description("응답 시간")
                        )
                ));
    }

    @Test
    void basicInfoTest() {
        webTestClient.get()
                .uri("/project/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProjectBasicInfo.class)
                .consumeWith(document("project/basic-info",
                        responseFields(
                                fieldWithPath("region[]").type("NameKey[]").description("지역 이름 및 키값 배열"),
                                fieldWithPath("region[].name").type("String").description("지역 이름"),
                                fieldWithPath("region[].key").type("String").description("지역 키값"),
                                fieldWithPath("job[]").type("NameKeyValue[]").description("직군별 이름 및 키값, 세부 직군 배열"),
                                fieldWithPath("job[].name").type("String").description("직군 이름"),
                                fieldWithPath("job[].key").type("String").description("직군 키값"),
                                fieldWithPath("job[].value").type("NameKey[]").description("세부 직군 배열"),
                                fieldWithPath("job[].value[].name").type("String").description("세부 직군 이름"),
                                fieldWithPath("job[].value[].key").type("String").description("세부 직군 키값"),
                                fieldWithPath("skill[]").type("String[]").description("스킬 배열"),
                                fieldWithPath("category[]").type("NameKey[]").description("프로젝트 분야 이름 및 키값 배열"),
                                fieldWithPath("category[].name").type("String").description("프로젝트 분야 이름"),
                                fieldWithPath("category[].key").type("String").description("프로젝트 분야 키값")
                        )
                ));
    }

    @Test
    void favoriteTest() {
        String token = JwtProvider.BEARER_PREFIX + "access token";
        when(jwtProvider.getId(anyString())).thenReturn(0L);
        when(userRequest.setFavorite(anyLong(), any(FavoriteType.class), anyLong())).thenReturn(Mono.just(true));
        when(projectRequest.updateFavorite(any(FavoriteUpdateInput.class))).thenReturn(Mono.just(10));

        webTestClient.post()
                .uri("/project/favorite")
                .header(JwtProvider.AUTH_HEADER, token)
                .bodyValue(new ProjectFavoriteParam(1L))
                .exchange()
                .expectStatus().isOk()
                .expectBody(FavoriteRes.class)
                .consumeWith(document("project/favorite",
                        requestHeaders(
                                headerWithName(JwtProvider.AUTH_HEADER).description(JwtProvider.BEARER_PREFIX + "Access Token")
                        ),
                        requestFields(
                                fieldWithPath("projectId").type("Number").description("프로젝트 아이디")
                        ),
                        responseFields(
                                fieldWithPath("project").type("Number").description("프로젝트 아이디"),
                                fieldWithPath("myFavorite").type("Boolean").description("좋아요 여부"),
                                fieldWithPath("favorite").type("Number").description("좋아요 수")
                        )
                ));
    }

    @Test
    void applyTest() {
        String token = JwtProvider.BEARER_PREFIX + "access token";
        when(jwtProvider.getId(anyString())).thenReturn(0L);
        when(projectRequest.applyProject(any(ApplyInput.class))).thenReturn(Mono.just(true));

        webTestClient.post()
                .uri("/project/apply")
                .header(JwtProvider.AUTH_HEADER, token)
                .bodyValue(new ApplyParam(1L, MemberPart.PL_PM_PO, "지원 메시지"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(BooleanValueRes.class)
                .consumeWith(document("project/apply",
                        requestHeaders(
                                headerWithName(JwtProvider.AUTH_HEADER).description(JwtProvider.BEARER_PREFIX + "Access Token")
                        ),
                        requestFields(
                                fieldWithPath("projectId").type("Number").description("프로젝트 아이디"),
                                fieldWithPath("part").type("String").description("지원 직군"),
                                fieldWithPath("message").type("String").description("지원 메시지")
                        ),
                        responseFields(
                                fieldWithPath("value").type("Boolean").description("지원 여부")
                        )
                ));
    }

    @Test
    void notfoundApplyTest() {
        String token = JwtProvider.BEARER_PREFIX + "access token";
        when(jwtProvider.getId(anyString())).thenReturn(0L);
        when(projectRequest.applyProject(any(ApplyInput.class))).thenReturn(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PART.toString())));

        webTestClient.post()
                .uri("/project/apply")
                .header(JwtProvider.AUTH_HEADER, token)
                .bodyValue(new ApplyParam(1L, MemberPart.PL_PM_PO, "지원 메시지"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorInfo.class)
                .consumeWith(document("project/apply-notfound",
                        responseFields(
                                fieldWithPath("path").type("String").description("요청 경로"),
                                fieldWithPath("status").type("Integer").description("응답 코드"),
                                fieldWithPath("error").type("String").description("에러 유형"),
                                fieldWithPath("message").type("String").description("실패 메시지"),
                                fieldWithPath("timestamp").type("Datetime").description("응답 시간")
                        )
                ));
    }

    @Test
    void invalidApplyTest() {
        String token = JwtProvider.BEARER_PREFIX + "access token";
        when(jwtProvider.getId(anyString())).thenReturn(0L);
        when(projectRequest.applyProject(any(ApplyInput.class))).thenReturn(Mono.error(new IllegalArgumentException(ProjectExceptionMessage.COLLECTED_PART.toString())));

        webTestClient.post()
                .uri("/project/apply")
                .header(JwtProvider.AUTH_HEADER, token)
                .bodyValue(new ApplyParam(1L, MemberPart.PL_PM_PO, "지원 메시지"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class)
                .consumeWith(document("project/apply-invalid",
                        responseFields(
                                fieldWithPath("path").type("String").description("요청 경로"),
                                fieldWithPath("status").type("Integer").description("응답 코드"),
                                fieldWithPath("error").type("String").description("에러 유형"),
                                fieldWithPath("message").type("String").description("실패 메시지"),
                                fieldWithPath("timestamp").type("Datetime").description("응답 시간")
                        )
                ));
    }

    @Test
    void reportTest() {
        String token = JwtProvider.BEARER_PREFIX + "access token";
        when(jwtProvider.getId(anyString())).thenReturn(0L);
        when(projectRequest.reportProject(any(ReportInput.class))).thenReturn(Mono.just(true));

        webTestClient.post()
                .uri("/project/report")
                .header(JwtProvider.AUTH_HEADER, token)
                .bodyValue(new ReportParam(1L, "신고 메시지"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(BooleanValueRes.class)
                .consumeWith(document("project/report",
                        requestHeaders(
                                headerWithName(JwtProvider.AUTH_HEADER).description(JwtProvider.BEARER_PREFIX + "Access Token")
                        ),
                        requestFields(
                                fieldWithPath("projectId").type("Number").description("프로젝트 아이디"),
                                fieldWithPath("reason").type("String").description("신고 사유")
                        ),
                        responseFields(
                                fieldWithPath("value").type("Boolean").description("신고 여부")
                        )
                ));
    }

    @Test
    void notfoundReportTest() {
        String token = JwtProvider.BEARER_PREFIX + "access token";
        when(jwtProvider.getId(anyString())).thenReturn(0L);
        when(projectRequest.reportProject(any(ReportInput.class))).thenReturn(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString())));

        webTestClient.post()
                .uri("/project/report")
                .header(JwtProvider.AUTH_HEADER, token)
                .bodyValue(new ReportParam(1L, "신고 메시지"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorInfo.class)
                .consumeWith(document("project/report-notfound",
                        responseFields(
                                fieldWithPath("path").type("String").description("요청 경로"),
                                fieldWithPath("status").type("Integer").description("응답 코드"),
                                fieldWithPath("error").type("String").description("에러 유형"),
                                fieldWithPath("message").type("String").description("실패 메시지"),
                                fieldWithPath("timestamp").type("Datetime").description("응답 시간")
                        )
                ));
    }

    @Test
    void invalidReportTest() {
        String token = JwtProvider.BEARER_PREFIX + "access token";
        when(jwtProvider.getId(anyString())).thenReturn(0L);
        when(projectRequest.reportProject(any(ReportInput.class))).thenReturn(Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ALREADY_REPORT.toString())));

        webTestClient.post()
                .uri("/project/report")
                .header(JwtProvider.AUTH_HEADER, token)
                .bodyValue(new ReportParam(1L, "신고 메시지"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorInfo.class)
                .consumeWith(document("project/report-invalid",
                        responseFields(
                                fieldWithPath("path").type("String").description("요청 경로"),
                                fieldWithPath("status").type("Integer").description("응답 코드"),
                                fieldWithPath("error").type("String").description("에러 유형"),
                                fieldWithPath("message").type("String").description("실패 메시지"),
                                fieldWithPath("timestamp").type("Datetime").description("응답 시간")
                        )
                ));
    }

    @AfterAll
    static void clearBannerForTest() {
        try (var pathStream = Files.walk(Paths.get(BANNER_PATH), Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)) {
            pathStream.forEach(path -> {
                if (path.toFile().isFile()) {
                    try {
                        Files.delete(path);
                        System.out.println("delete file : " + path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            File file = new File(BANNER_PATH);
            if (file.exists() && file.delete()) {
                System.out.println("banner 폴더 제거");
            }
        } catch (IOException e) {
            e.printStackTrace(); // 디렉토리 탐색 중 예외 처리
        }
    }
}



