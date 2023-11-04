package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.api.project.ProjectDetail;
import com.connectcrew.teamone.api.project.ProjectFilterOption;
import com.connectcrew.teamone.api.project.ProjectInput;
import com.connectcrew.teamone.api.project.ProjectItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectRequest {
    Flux<ProjectItem> getProjectList(ProjectFilterOption option);

    Mono<ProjectDetail> getProjectDetail(Long projectId);

    Mono<Long> saveProject(ProjectInput input);
}
