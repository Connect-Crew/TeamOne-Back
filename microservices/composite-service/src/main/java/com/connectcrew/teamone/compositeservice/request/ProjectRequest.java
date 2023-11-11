package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.api.project.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectRequest {
    Flux<ProjectItem> getProjectList(ProjectFilterOption option);

    Mono<ProjectDetail> getProjectDetail(Long projectId);

    Mono<Long> saveProject(ProjectInput input);

    Mono<Boolean> applyProject(ApplyInput input);

    Mono<Boolean> reportProject(ReportInput input);
}
