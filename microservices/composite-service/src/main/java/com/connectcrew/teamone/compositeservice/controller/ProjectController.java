package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.project.*;
import com.connectcrew.teamone.api.user.favorite.FavoriteType;
import com.connectcrew.teamone.api.user.profile.Profile;
import com.connectcrew.teamone.compositeservice.auth.JwtProvider;
import com.connectcrew.teamone.compositeservice.param.ApplyParam;
import com.connectcrew.teamone.compositeservice.param.ProjectFavoriteParam;
import com.connectcrew.teamone.compositeservice.param.ProjectInputParam;
import com.connectcrew.teamone.compositeservice.param.ReportParam;
import com.connectcrew.teamone.compositeservice.request.FavoriteRequest;
import com.connectcrew.teamone.compositeservice.request.ProjectRequest;
import com.connectcrew.teamone.compositeservice.request.UserRequest;
import com.connectcrew.teamone.compositeservice.resposne.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final JwtProvider jwtProvider;
    private final UserRequest userRequest;
    private final ProjectRequest projectRequest;

    private final FavoriteRequest favoriteRequest;

    private final ProjectBasicInfo projectBasicInfo;

    public ProjectController(JwtProvider provider, UserRequest userRequest, ProjectRequest projectRequest, FavoriteRequest favoriteRequest) {
        this.jwtProvider = provider;
        this.userRequest = userRequest;
        this.projectRequest = projectRequest;
        this.favoriteRequest = favoriteRequest;
        this.projectBasicInfo = new ProjectBasicInfo();
    }

    @GetMapping("/")
    public ProjectBasicInfo getProjectBasicInfo() {
        return projectBasicInfo;
    }

    @GetMapping("/list")
    private Mono<List<ProjectItemRes>> getProjectList(@RequestHeader(JwtProvider.AUTH_HEADER) String token, ProjectFilterOption option) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        System.out.println(option);
        return projectRequest.getProjectList(option)
                .collectList()
                .flatMap(projects -> {
                    if (projects.size() == 0) {
                        return Mono.just(Tuples.of(projects, new HashMap<Long, Boolean>()));
                    }
                    List<Long> profileIds = projects.stream().map(ProjectItem::id).toList();

                    return favoriteRequest.isFavorite(id, FavoriteType.PROJECT, profileIds)
                            .map(favoriteMap -> Tuples.of(projects, favoriteMap));
                })
                .map(tuple -> {
                    Map<Long, Boolean> favoriteMap = tuple.getT2();
                    return tuple.getT1().stream()
                            .map(project -> {
                                Boolean isFavorite = favoriteMap.getOrDefault(project.id(), false);

                                return new ProjectItemRes(project, isFavorite);
                            })
                            .toList();
                });
    }

    @GetMapping("/{projectId}")
    private Mono<ProjectDetailRes> getProjectDetail(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @PathVariable Long projectId) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return projectRequest.getProjectDetail(projectId)
                .flatMap(project -> {
                    Set<Long> profileIds = new HashSet<>();
                    profileIds.add(project.leader());
                    profileIds.addAll(project.members().stream().map(ProjectMember::memberId).toList());

                    return Flux.fromIterable(profileIds)
                            .flatMap(userRequest::getProfile)
                            .collectMap(Profile::id, p -> p)
                            .map(profileMap -> Tuples.of(project, profileMap));
                })
                .flatMap(tuple -> {
                    ProjectDetail project = tuple.getT1();
                    Map<Long, Profile> profileMap = tuple.getT2();

                    return favoriteRequest.isFavorite(id, FavoriteType.PROJECT, projectId)
                            .map(favorite -> Tuples.of(project, favorite, profileMap));
                })
                .map(tuple -> new ProjectDetailRes(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    @PostMapping("/")
    private Mono<LongValueRes> createProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ProjectInputParam param) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        // TODO 베너 이미지 저장

        ProjectInput input = new ProjectInput(
                param.title(),
                new ArrayList<>(),
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

        return projectRequest.saveProject(input)
                .map(LongValueRes::new);
    }

    @PostMapping("/favorite")
    private Mono<BooleanValueRes> favoriteProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ProjectFavoriteParam param) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return favoriteRequest.setFavorite(id, FavoriteType.PROJECT, param.projectId()).map(BooleanValueRes::new);
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