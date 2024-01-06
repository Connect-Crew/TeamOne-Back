package com.connectcrew.teamone.compositeservice.composite.adapter.in.web;

import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.auth.domain.TokenClaim;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.*;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.*;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.*;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateChatRoomCommand;
import com.connectcrew.teamone.compositeservice.composite.domain.Apply;
import com.connectcrew.teamone.compositeservice.composite.domain.ApplyStatus;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectFavorite;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectItem;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.*;
import com.connectcrew.teamone.compositeservice.file.application.port.in.DeleteFileUseCase;
import com.connectcrew.teamone.compositeservice.file.application.port.in.QueryFileUseCase;
import com.connectcrew.teamone.compositeservice.file.application.port.in.SaveFileUseCase;
import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;
import com.connectcrew.teamone.compositeservice.global.enums.ChatRoomType;
import com.connectcrew.teamone.compositeservice.global.enums.FavoriteType;
import com.connectcrew.teamone.compositeservice.global.enums.Region;
import com.connectcrew.teamone.compositeservice.global.error.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.compositeservice.global.error.enums.ErrorLevel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private static final ProjectBasicInfoResponse projectBasicInfo = new ProjectBasicInfoResponse();

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final CreateChatRoomUseCase createChatRoomUseCase;
    private final SaveUserUseCase saveUserUseCase;
    private final SaveFileUseCase saveFileUseCase;
    private final SaveProjectUseCase saveProjectUseCase;
    private final QueryFileUseCase queryFileUseCase;
    private final QueryUserUseCase queryUserUseCase;
    private final QueryProjectUseCase queryProjectUseCase;
    private final UpdateProjectUseCase updateProjectUseCase;
    private final QueryProfileUseCase queryProfileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;
    private final SendErrorNotificationUseCase sendErrorNotificationUseCase;

    @GetMapping("/")
    public ProjectBasicInfoResponse getProjectBasicInfo() {
        return projectBasicInfo;
    }

    @GetMapping("/banner/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        try {
            MediaType mediaType = queryFileUseCase.findContentType(filename);
            Resource resource = queryFileUseCase.find(FileCategory.BANNER, filename);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);
        } catch (Exception e) {
            sendErrorNotificationUseCase.send("ProjectController.getImage", ErrorLevel.ERROR, e);
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/list")
    private Mono<List<ProjectItemResponse>> getProjectList(@RequestHeader(JwtProvider.AUTH_HEADER) String token, ProjectListRequest request) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return queryProjectUseCase.getProjectList(request.toQuery())
                .flatMap(projects -> queryUserUseCase.isFavorite(id, FavoriteType.PROJECT, projects.stream().map(ProjectItem::id).toList())
                        .map(favoriteMap -> Tuples.of(projects, favoriteMap)))
                .map(tuple -> tuple.getT1().stream()
                        .map(project ->
                                ProjectItemResponse.from(project, tuple.getT2().getOrDefault(project.id(), false))
                        ).toList()
                )
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectList", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/mylist")
    private Mono<List<ProjectItemResponse>> getProjectList(@RequestHeader(JwtProvider.AUTH_HEADER) String token) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return queryProjectUseCase.getProjectList(id)
                .flatMap(projects -> queryUserUseCase.isFavorite(id, FavoriteType.PROJECT, projects.stream().map(ProjectItem::id).toList())
                        .map(favoriteMap -> Tuples.of(projects, favoriteMap)))
                .map(tuple -> tuple.getT1().stream()
                        .map(project ->
                                ProjectItemResponse.from(project, tuple.getT2().getOrDefault(project.id(), false))
                        ).toList()
                )
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectList", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/{projectId}")
    private Mono<ProjectDetailResponse> getProjectDetail(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @PathVariable Long projectId) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return queryProjectUseCase.find(projectId, id)
                .flatMap(project -> queryProfileUseCase.getFullProfile(project.leader())
                        .map(l -> l.update(project.leaderParts()))
                        .map(profile -> Tuples.of(project, profile))
                )
                .flatMap(tuple -> queryUserUseCase.isFavorite(id, FavoriteType.PROJECT, projectId).map(favorite -> Tuples.of(tuple.getT1(), favorite, tuple.getT2())))
                .map(tuple -> {
                    List<String> banners = tuple.getT1().banners().stream().map(FileCategory.BANNER::getUrlPath).toList();
                    return new ProjectDetailResponse(tuple.getT1(), banners, tuple.getT2(), ProfileResponse.from(tuple.getT3()));
                })
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectDetail", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/members/{projectId}")
    private Mono<List<ProjectMemberResponse>> getProjectMembers(@PathVariable Long projectId) {
        return queryProjectUseCase.getProjectMemberList(projectId)
                .doOnNext(m -> log.trace("getProjectMembers - member: {}", m))
                .flatMap(member -> queryProfileUseCase.getFullProfile(member.userId())
                        .map(ProfileResponse::from)
                        .map(profileRes -> new ProjectMemberResponse(member, profileRes)))
                .collectList()
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectMembers", ErrorLevel.ERROR, ex));
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private Mono<SimpleLongResponse> createProject(
            @RequestHeader(JwtProvider.AUTH_HEADER) String token,
            @RequestPart(value = "banner", required = false) Flux<FilePart> banner,
            @RequestPart String title,
            @RequestPart String region,
            @RequestPart String online,
            @RequestPart String state,
            @RequestPart String careerMin,
            @RequestPart String careerMax,
            @RequestPart Flux<String> leaderParts,
            @RequestPart Flux<String> category,
            @RequestPart String goal,
            @RequestPart String introduction,
            @RequestPart Flux<String> recruits,
            @RequestPart Flux<String> skills
    ) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return Mono.zip(
                        leaderParts.collectList(),
                        category.collectList(),
                        recruits.map(r -> {
                            try {
                                return objectMapper.readValue(r, CreateRecruitRequest.class);
                            } catch (JsonProcessingException e) {
                                log.error("createProject - recruit json parse error: {}", e.getMessage(), e);
                                throw new RuntimeException(e);
                            }
                        }).collectList(),
                        skills.collectList()
                )
                .doOnNext(tuple -> log.trace("createProject - title: {}, region: {}, online: {}, state: {}, careerMin: {}, careerMax: {}, leaderParts: {}, category: {}, goal: {}, introduction: {}, recruits: {}, skills: {}",
                        title, region, online, state, careerMin, careerMax, tuple.getT1(), tuple.getT2(), goal, introduction, tuple.getT3(), tuple.getT4()))
                .map(tuple -> CreateProjectRequest.builder()
                        .title(removeQuotation(title))
                        .region(Region.valueOf(removeQuotation(region)))
                        .online(Boolean.valueOf(online))
                        .state(ProjectState.valueOf(removeQuotation(state)))
                        .careerMin(Career.valueOf(removeQuotation(careerMin)))
                        .careerMax(Career.valueOf(removeQuotation(careerMax)))
                        .leaderParts(tuple.getT1().stream().map(this::removeQuotation).map(MemberPart::valueOf).toList())
                        .category(tuple.getT2().stream().map(this::removeQuotation).map(ProjectCategory::valueOf).toList())
                        .goal(ProjectGoal.valueOf(removeQuotation(goal)))
                        .introduction(removeQuotation(introduction))
                        .recruits(tuple.getT3())
                        .skills(tuple.getT4().stream().map(this::removeQuotation).toList())
                        .build())
                .flatMap(request -> saveFileUseCase.saveBanners(FileCategory.BANNER, banner).collectList()
                        .doOnNext(bannerPaths -> log.trace("createProject - bannerPaths: {}", bannerPaths))
                        .flatMap(bannerPaths -> createChatRoomUseCase.createChatRoom(new CreateChatRoomCommand(ChatRoomType.PROJECT, Set.of(id))).map(res -> Tuples.of(bannerPaths, res)))
                        .flatMap(tuple -> saveProjectUseCase.save(request.toCommand(id, tuple.getT2().id(), tuple.getT1()))
                                .onErrorResume(ex -> deleteFileUseCase.deleteBanners(FileCategory.BANNER, tuple.getT1()).then(Mono.error(ex)))) // 프로젝트 글 작성 실패시 저장된 배너 삭제
                        // TODO 프로젝트 글 작성 실패시 채팅방 삭제
                        .map(SimpleLongResponse::new))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.createProject", ErrorLevel.ERROR, ex));
    }

    private String removeQuotation(String str) {
        String result = str;
        if (str.startsWith("\""))
            result = str.substring(1);
        if (str.endsWith("\""))
            result = result.substring(0, result.length() - 1);
        return result;
    }

    @PutMapping(value = "/{projectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private Mono<SimpleLongResponse> modifyProject(
            @RequestHeader(JwtProvider.AUTH_HEADER) String token,
            @PathVariable Long projectId,
            @RequestPart(value = "banner", required = false) Flux<FilePart> banner,
            @RequestPart String title,
            @RequestPart String region,
            @RequestPart String online,
            @RequestPart String state,
            @RequestPart String careerMin,
            @RequestPart String careerMax,
            @RequestPart Flux<String> leaderParts,
            @RequestPart Flux<String> category,
            @RequestPart String goal,
            @RequestPart String introduction,
            @RequestPart Flux<String> recruits,
            @RequestPart Flux<String> skills
    ) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long userId = claim.id();

        return Mono.zip(
                        leaderParts.collectList(),
                        category.collectList(),
                        recruits.map(r -> {
                            try {
                                return objectMapper.readValue(r, CreateRecruitRequest.class);
                            } catch (JsonProcessingException e) {
                                log.error("createProject - recruit json parse error: {}", e.getMessage(), e);
                                throw new RuntimeException(e);
                            }
                        }).collectList(),
                        skills.collectList()
                )
                .doOnNext(tuple -> log.trace("modifyProject - title: {}, region: {}, online: {}, state: {}, careerMin: {}, careerMax: {}, leaderParts: {}, category: {}, goal: {}, introduction: {}, recruits: {}, skills: {}",
                        title, region, online, state, careerMin, careerMax, tuple.getT1(), tuple.getT2(), goal, introduction, tuple.getT3(), tuple.getT4()))
                .map(tuple -> ModifyProjectRequest.builder()
                        .projectId(projectId)
                        .userId(userId)
                        .title(removeQuotation(title))
                        .region(Region.valueOf(removeQuotation(region)))
                        .online(Boolean.valueOf(online))
                        .state(ProjectState.valueOf(removeQuotation(state)))
                        .careerMin(Career.valueOf(removeQuotation(careerMin)))
                        .careerMax(Career.valueOf(removeQuotation(careerMax)))
                        .leaderParts(tuple.getT1().stream().map(this::removeQuotation).map(MemberPart::valueOf).toList())
                        .category(tuple.getT2().stream().map(this::removeQuotation).map(ProjectCategory::valueOf).toList())
                        .goal(ProjectGoal.valueOf(removeQuotation(goal)))
                        .introduction(removeQuotation(introduction))
                        .recruits(tuple.getT3())
                        .skills(tuple.getT4().stream().map(this::removeQuotation).toList())
                        .build())
                .flatMap(request -> saveFileUseCase.saveBanners(FileCategory.BANNER, banner).collectList()
                        .doOnNext(bannerPaths -> log.trace("modifyProject - bannerPaths: {}", bannerPaths))
                        .flatMap(bannerPaths -> updateProjectUseCase.update(request.toCommand(bannerPaths))
                                .onErrorResume(ex -> deleteFileUseCase.deleteBanners(FileCategory.BANNER, bannerPaths).then(Mono.error(ex)))) // 프로젝트 글 작성 실패시 저장된 배너 삭제
                        // TODO 프로젝트 글 작성 실패시 채팅방 삭제
                        .map(SimpleLongResponse::new))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.createProject", ErrorLevel.ERROR, ex));
    }


    @PostMapping("/favorite")
    private Mono<FavoriteResponse> favoriteProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ProjectFavoriteRequest param) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();
        return queryUserUseCase.isFavorite(id, FavoriteType.PROJECT, param.projectId())
                .flatMap(favorite -> saveProjectUseCase.setFavorite(new ProjectFavorite(param.projectId(), !favorite)))
                .flatMap(newFavorite -> saveUserUseCase.setFavorite(id, FavoriteType.PROJECT, param.projectId())
                        .map(favorite -> new FavoriteResponse(param.projectId(), favorite, newFavorite)))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.favoriteProject", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/apply/{projectId}")
    private Mono<Map<MemberPart, ApplyStatusResponse>> getApplyStatus(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @PathVariable Long projectId) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return queryProjectUseCase.getApplyStatus(id, projectId)
                .collectMap(ApplyStatus::part, ApplyStatus::toResponse);
    }

    @GetMapping("/apply/{projectId}/{part}")
    private Mono<List<ApplyResponse>> getApplies(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @PathVariable Long projectId, @PathVariable MemberPart part) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return queryProjectUseCase.getApplies(id, projectId, part)
                .map(Apply::toResponse)
                .collectList();
    }

    @PostMapping("/apply")
    private Mono<SimpleBooleanResponse> applyProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ApplyRequest param) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return saveProjectUseCase.save(param.toDomain(id)).map(SimpleBooleanResponse::new)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.applyProject", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/report")
    private Mono<SimpleBooleanResponse> reportProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ReportRequest param) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return saveProjectUseCase.save(param.toDomain(id)).map(SimpleBooleanResponse::new)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.reportProject", ErrorLevel.ERROR, ex));
    }
}
