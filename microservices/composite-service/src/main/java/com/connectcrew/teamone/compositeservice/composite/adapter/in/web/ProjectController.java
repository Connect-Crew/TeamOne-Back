package com.connectcrew.teamone.compositeservice.composite.adapter.in.web;

import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.auth.domain.TokenClaim;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.ProjectListRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.*;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.*;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateChatRoomCommand;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectFavorite;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectItem;
import com.connectcrew.teamone.compositeservice.file.application.port.in.DeleteFileUseCase;
import com.connectcrew.teamone.compositeservice.file.application.port.in.QueryFileUseCase;
import com.connectcrew.teamone.compositeservice.file.application.port.in.SaveFileUseCase;
import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;
import com.connectcrew.teamone.compositeservice.global.enums.ChatRoomType;
import com.connectcrew.teamone.compositeservice.global.enums.FavoriteType;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.ApplyRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.ProjectFavoriteRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.CreateProjectRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.ReportRequest;
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
                .flatMap(member -> queryProfileUseCase.getFullProfile(member.memberId())
                        .map(ProfileResponse::from)
                        .map(profileRes -> new ProjectMemberResponse(member, profileRes)))
                .collectList();
    }

    @PostMapping("/")
    private Mono<SimpleLongResponse> createProject(
            @RequestHeader(JwtProvider.AUTH_HEADER) String token,
            @RequestPart("banner") Flux<FilePart> banner,
            @RequestPart("param") CreateProjectRequest param
    ) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return saveFileUseCase.saveBanners(FileCategory.BANNER, banner).collectList()
                .flatMap(bannerPaths -> createChatRoomUseCase.createChatRoom(new CreateChatRoomCommand(ChatRoomType.PROJECT, Set.of(id))).map(res -> Tuples.of(bannerPaths, res)))
                .flatMap(tuple -> saveProjectUseCase.save(param.toCommand(id, tuple.getT2().id()))
                        .onErrorResume(ex -> deleteFileUseCase.deleteBanners(FileCategory.BANNER, tuple.getT1()).then(Mono.error(ex)))) // 프로젝트 글 작성 실패시 저장된 배너 삭제
                        // TODO 프로젝트 글 작성 실패시 채팅방 삭제
                .map(SimpleLongResponse::new);
    }


    @PostMapping("/favorite")
    private Mono<FavoriteResponse> favoriteProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ProjectFavoriteRequest param) { // TODO param에 favorite 여부 제거
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return saveProjectUseCase.setFavorite(new ProjectFavorite(param.projectId(), param.favorite()))
                .flatMap(newFavorite -> saveUserUseCase.setFavorite(id, FavoriteType.PROJECT, param.projectId())
                        .map(favorite -> new FavoriteResponse(param.projectId(), favorite, newFavorite)));
    }

    @PostMapping("/apply")
    private Mono<SimpleBooleanResponse> applyProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ApplyRequest param) {
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
