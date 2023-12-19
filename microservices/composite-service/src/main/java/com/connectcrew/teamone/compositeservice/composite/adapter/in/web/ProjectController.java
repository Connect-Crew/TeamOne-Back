package com.connectcrew.teamone.compositeservice.composite.adapter.in.web;

import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.auth.domain.TokenClaim;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.*;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.*;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.*;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateChatRoomCommand;
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
    private final QueryProfileUseCase queryProfileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;

    @GetMapping("/")
    public ProjectBasicInfoResponse getProjectBasicInfo() {
        return projectBasicInfo;
    }

    @GetMapping("/banner/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        MediaType mediaType = queryFileUseCase.findContentType(filename);
        Resource resource = queryFileUseCase.find(FileCategory.BANNER, filename);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
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
                );
    }

    @GetMapping("/{projectId}")
    private Mono<ProjectDetailResponse> getProjectDetail(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @PathVariable Long projectId) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return queryProjectUseCase.find(projectId, id)
                .flatMap(project -> queryProfileUseCase.getFullProfile(project.leader()).map(profile -> Tuples.of(project, profile)))
                .flatMap(tuple -> queryUserUseCase.isFavorite(id, FavoriteType.PROJECT, projectId).map(favorite -> Tuples.of(tuple.getT1(), favorite, tuple.getT2())))
                .map(tuple -> {
                    List<String> banners = tuple.getT1().banners().stream().map(FileCategory.BANNER::getUrlPath).toList();
                    return new ProjectDetailResponse(tuple.getT1(), banners, tuple.getT2(), ProfileResponse.from(tuple.getT3()));
                });
    }

    @GetMapping("/members/{projectId}")
    private Mono<List<ProjectMemberResponse>> getProjectMembers(@PathVariable Long projectId) {
        return queryProjectUseCase.getProjectMemberList(projectId)
                .flatMapMany(Flux::fromIterable)
                .doOnNext(member -> System.out.println(member))
                .flatMap(member -> queryProfileUseCase.getFullProfile(member.memberId())
                        .map(ProfileResponse::from)
                        .map(profileRes -> new ProjectMemberResponse(member, profileRes)))
                .collectList();
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

        System.out.println(state);

        return Mono.zip(
                        leaderParts.map(MemberPart::valueOf).collectList(),
                        category.map(ProjectCategory::valueOf).collectList(),
                        recruits.map(r -> {
                            try {
                                return objectMapper.readValue(r, CreateRecruitRequest.class);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }).collectList(),
                        skills.collectList()
                ).map(tuple -> CreateProjectRequest.builder()
                        .title(title)
                        .region(Region.valueOf(region))
                        .online(Boolean.valueOf(online))
                        .state(ProjectState.valueOf(state))
                        .careerMin(Career.valueOf(careerMin))
                        .careerMax(Career.valueOf(careerMax))
                        .leaderParts(tuple.getT1())
                        .category(tuple.getT2())
                        .goal(ProjectGoal.valueOf(goal))
                        .introduction(introduction)
                        .recruits(tuple.getT3())
                        .skills(tuple.getT4())
                        .build())
                .flatMap(request -> saveFileUseCase.saveBanners(FileCategory.BANNER, banner).collectList()
                        .flatMap(bannerPaths -> createChatRoomUseCase.createChatRoom(new CreateChatRoomCommand(ChatRoomType.PROJECT, Set.of(id))).map(res -> Tuples.of(bannerPaths, res)))
                        .flatMap(tuple -> saveProjectUseCase.save(request.toCommand(id, tuple.getT2().id()))
                                .onErrorResume(ex -> deleteFileUseCase.deleteBanners(FileCategory.BANNER, tuple.getT1()).then(Mono.error(ex)))) // 프로젝트 글 작성 실패시 저장된 배너 삭제
                        // TODO 프로젝트 글 작성 실패시 채팅방 삭제
                        .map(SimpleLongResponse::new));
    }


    @PostMapping("/favorite")
    private Mono<FavoriteResponse> favoriteProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ProjectFavoriteRequest param) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();
        return queryUserUseCase.isFavorite(id, FavoriteType.PROJECT, param.projectId())
                .flatMap(favorite -> saveProjectUseCase.setFavorite(new ProjectFavorite(param.projectId(), !favorite)))
                .flatMap(newFavorite -> saveUserUseCase.setFavorite(id, FavoriteType.PROJECT, param.projectId())
                        .map(favorite -> new FavoriteResponse(param.projectId(), favorite, newFavorite)));
    }

    @PostMapping("/apply")
    private Mono<SimpleBooleanResponse> applyProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ApplyRequest param) {
        System.out.println("Test!!!");
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return saveProjectUseCase.save(param.toDomain(id)).map(SimpleBooleanResponse::new);
    }

    @PostMapping("/report")
    private Mono<SimpleBooleanResponse> reportProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ReportRequest param) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return saveProjectUseCase.save(param.toDomain(id)).map(SimpleBooleanResponse::new);
    }
}
