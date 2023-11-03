package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.project.ProjectFilterOption;
import com.connectcrew.teamone.compositeservice.request.ProfileRequest;
import com.connectcrew.teamone.compositeservice.request.ProjectRequest;
import com.connectcrew.teamone.compositeservice.resposne.ProjectBasicInfo;
import com.connectcrew.teamone.compositeservice.resposne.ProjectDetailRes;
import com.connectcrew.teamone.compositeservice.resposne.ProjectItemRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProfileRequest profileRequest;
    private final ProjectRequest projectRequest;

    private final ProjectBasicInfo projectBasicInfo;

    public ProjectController(ProfileRequest profileRequest, ProjectRequest projectRequest) {
        this.profileRequest = profileRequest;
        this.projectRequest = projectRequest;
        this.projectBasicInfo = new ProjectBasicInfo();
    }

    @GetMapping("/")
    public ProjectBasicInfo getProjectBasicInfo() {
        return projectBasicInfo;
    }

    @GetMapping("/list")
    private Mono<List<ProjectItemRes>> getProjectList(@RequestParam(required = false, defaultValue = "-1") int lastId, int size, @RequestBody(required = false) ProjectFilterOption option) {
        return projectRequest.getProjectList(lastId, size, option)
                .map(ProjectItemRes::new)
                .collectList();
    }

    @GetMapping("/{projectId}")
    private Mono<ProjectDetailRes> getProjectDetail(@PathVariable Long projectId) {
        return projectRequest.getProjectDetail(projectId)
                // TODO 향후 Profile 정보 추가
                .map(ProjectDetailRes::new);
    }

}