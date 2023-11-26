package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.api.project.FavoriteUpdateInput;
import com.connectcrew.teamone.api.project.ProjectFilterOption;
import com.connectcrew.teamone.api.project.ProjectInput;
import com.connectcrew.teamone.api.project.ProjectItem;
import com.connectcrew.teamone.api.user.favorite.FavoriteType;
import com.connectcrew.teamone.compositeservice.auth.JwtProvider;
import com.connectcrew.teamone.compositeservice.param.ApplyParam;
import com.connectcrew.teamone.compositeservice.param.ProjectFavoriteParam;
import com.connectcrew.teamone.compositeservice.param.ProjectInputParam;
import com.connectcrew.teamone.compositeservice.param.ReportParam;
import com.connectcrew.teamone.compositeservice.request.FavoriteRequest;
import com.connectcrew.teamone.compositeservice.request.ProjectRequest;
import com.connectcrew.teamone.compositeservice.resposne.*;
import com.connectcrew.teamone.compositeservice.service.BannerService;
import com.connectcrew.teamone.compositeservice.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final JwtProvider jwtProvider;
    private final ProjectRequest projectRequest;

    private final FavoriteRequest favoriteRequest;

    private final ProjectBasicInfo projectBasicInfo;
    private final ProfileService profileService;
    private final BannerService bannerService;

    public ProjectController(JwtProvider provider, ProjectRequest projectRequest, FavoriteRequest favoriteRequest, ProfileService profileService, BannerService bannerService) {
        this.jwtProvider = provider;
        this.projectRequest = projectRequest;
        this.favoriteRequest = favoriteRequest;
        this.projectBasicInfo = new ProjectBasicInfo();
        this.profileService = profileService;
        this.bannerService = bannerService;
    }

    @GetMapping("/")
    public ProjectBasicInfo getProjectBasicInfo() {
        return projectBasicInfo;
    }

    @GetMapping("/banner/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        String[] fileNameAndExtensions = filename.split("\\.");
        String name = fileNameAndExtensions[0];
        String extension = fileNameAndExtensions[1];

        Optional<MediaType> mediaType = bannerService.getMediaType(extension);
        if (mediaType.isEmpty()) {
            throw new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_EXTENSION.toString());
        }

        Resource resource = bannerService.getBanner(name, extension);

        return ResponseEntity.ok()
                .contentType(mediaType.get())
                .body(resource);
    }


    @GetMapping("/list")
    private Mono<List<ProjectItemRes>> getProjectList(@RequestHeader(JwtProvider.AUTH_HEADER) String token, ProjectFilterOption option) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        log.trace("getProjectList: {}", option);
        return projectRequest.getProjectList(option)
                .collectList()
                .flatMap(projects -> getFavoriteMap(id, projects).map(favoriteMap -> Tuples.of(projects, favoriteMap)))
                .map(tuple -> itemToRes(tuple.getT1(), tuple.getT2()));
    }

    private Mono<Map<Long, Boolean>> getFavoriteMap(Long userId, List<ProjectItem> projects) {
        if (projects.size() == 0) {
            return Mono.just(new HashMap<>());
        }
        List<Long> profileIds = projects.stream().map(ProjectItem::id).toList();

        return favoriteRequest.isFavorite(userId, FavoriteType.PROJECT, profileIds);
    }

    private List<ProjectItemRes> itemToRes(List<ProjectItem> items, Map<Long, Boolean> favoriteMap) {
        return items.stream()
                .map(item -> new ProjectItemRes(
                        item,
                        favoriteMap.getOrDefault(item.id(), false),
                        bannerService.getBannerUrlPath(item.thumbnail())
                ))
                .toList();
    }

    @GetMapping("/{projectId}")
    private Mono<ProjectDetailRes> getProjectDetail(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @PathVariable Long projectId) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return projectRequest.getProjectDetail(projectId, id)
                .flatMap(project -> profileService.getProfileRes(project.leader())
                        .map(leaderProfile -> Tuples.of(project, leaderProfile))
                )
                .flatMap(tuple -> favoriteRequest.isFavorite(id, FavoriteType.PROJECT, projectId)
                        .map(favorite -> Tuples.of(tuple.getT1(), favorite, tuple.getT2())))
                .map(tuple -> {
                    List<String> banners = tuple.getT1().banners().stream().map(bannerService::getBannerUrlPath).toList();
                    return new ProjectDetailRes(tuple.getT1(), banners, tuple.getT2(), tuple.getT3());
                });
    }

    @GetMapping("/members/{projectId}")
    private Mono<List<ProjectMemberRes>> getProjectMembers(@PathVariable Long projectId) {
        return projectRequest.getProjectMembers(projectId)
                .flatMapMany(Flux::fromIterable)
                .flatMap(member -> profileService.getProfileRes(member.memberId())
                        .map(profileRes -> new ProjectMemberRes(member, profileRes)))
                .collectList();
    }


    @PostMapping("/")
    private Mono<LongValueRes> createProject(
            @RequestHeader(JwtProvider.AUTH_HEADER) String token,
            @RequestPart("banner") Flux<FilePart> banner,
            @RequestPart("param") ProjectInputParam param
    ) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return bannerService.saveBanners(banner)
                .collectList()
                .flatMap(paths ->
                        projectRequest.saveProject(getProjectInput(param, id, paths))
                                .onErrorResume(ex -> bannerService.deleteBanners(paths).then(Mono.error(ex))) // 프로젝트 글 작성 실패시 저장된 배너 삭제
                )
                .map(LongValueRes::new);
    }

    private ProjectInput getProjectInput(ProjectInputParam param, Long id, List<String> paths) {
        return new ProjectInput(
                param.title(),
                paths,
                param.region(),
                param.online(),
                param.state(),
                param.careerMin(),
                param.careerMax(),
                id,
                param.leaderParts(),
                param.category(),
                param.goal(),
                param.introduction(),
                param.recruits(),
                param.skills()
        );
    }


    @PostMapping("/favorite")
    private Mono<FavoriteRes> favoriteProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ProjectFavoriteParam param) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return favoriteRequest.setFavorite(id, FavoriteType.PROJECT, param.projectId())
                .flatMap(newFavorite -> projectRequest.updateFavorite(new FavoriteUpdateInput(param.projectId(), newFavorite ? 1 : -1))
                        .map(favorite -> new FavoriteRes(param.projectId(), newFavorite, favorite))
                );
    }

    @PostMapping("/apply")
    private Mono<BooleanValueRes> applyProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ApplyParam param) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return projectRequest.applyProject(param.toInput(id)).map(BooleanValueRes::new);
    }

    @PostMapping("/report")
    private Mono<BooleanValueRes> reportProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ReportParam param) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return projectRequest.reportProject(param.toInput(id)).map(BooleanValueRes::new);
    }
}