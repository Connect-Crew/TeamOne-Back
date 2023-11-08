package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.project.ProjectFilterOption;
import com.connectcrew.teamone.api.project.ProjectInput;
import com.connectcrew.teamone.api.project.ProjectMember;
import com.connectcrew.teamone.api.user.profile.Profile;
import com.connectcrew.teamone.compositeservice.auth.JwtProvider;
import com.connectcrew.teamone.compositeservice.param.ProjectInputParam;
import com.connectcrew.teamone.compositeservice.request.ProfileRequest;
import com.connectcrew.teamone.compositeservice.request.ProjectRequest;
import com.connectcrew.teamone.compositeservice.resposne.ProjectBasicInfo;
import com.connectcrew.teamone.compositeservice.resposne.ProjectDetailRes;
import com.connectcrew.teamone.compositeservice.resposne.ProjectItemRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController {
    private JwtProvider jwtProvider;
    private final ProfileRequest profileRequest;
    private final ProjectRequest projectRequest;

    private final ProjectBasicInfo projectBasicInfo;

    public ProjectController(JwtProvider provider, ProfileRequest profileRequest, ProjectRequest projectRequest) {
        this.jwtProvider = provider;
        this.profileRequest = profileRequest;
        this.projectRequest = projectRequest;
        this.projectBasicInfo = new ProjectBasicInfo();
    }

    @GetMapping("/")
    public ProjectBasicInfo getProjectBasicInfo() {
        return projectBasicInfo;
    }

    @GetMapping("/list")
    private Mono<List<ProjectItemRes>> getProjectList(ProjectFilterOption option) {
        return projectRequest.getProjectList(option)
                .map(ProjectItemRes::new)
                .collectList();
    }

    @GetMapping("/{projectId}")
    private Mono<ProjectDetailRes> getProjectDetail(@PathVariable Long projectId) {
        return projectRequest.getProjectDetail(projectId)
//                .flatMap(project -> profileRequest.getProfile(project.leader()).map(leader -> Tuples.of(project, leader)))
                .flatMap(project -> {
                    Set<Long> profileIds = new HashSet<>();
                    profileIds.add(project.leader());
                    profileIds.addAll(project.members().stream().map(ProjectMember::memberId).toList());

                    return Flux.fromIterable(profileIds)
                            .flatMap(profileRequest::getProfile)
                            .collectMap(Profile::id, p -> p)
                            .map(profileMap -> Tuples.of(project, profileMap));
                })
                .map(tuple -> new ProjectDetailRes(tuple.getT1(), tuple.getT2()));
    }

    @PostMapping("/")
    private Mono<Long> createProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ProjectInputParam param) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        // TODO 베너 이미지 저장

        ProjectInput input = new ProjectInput(
                param.title(),
                new ArrayList<>(),
                param.region(),
                param.online(),
                param.start(),
                param.end(),
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

        return projectRequest.saveProject(input);
    }
}