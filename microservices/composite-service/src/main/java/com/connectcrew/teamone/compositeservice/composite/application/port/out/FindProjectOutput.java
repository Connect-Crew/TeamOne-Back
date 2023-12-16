package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.api.project.ProjectDetail;
import com.connectcrew.teamone.api.project.ProjectFilterOption;
import com.connectcrew.teamone.api.project.ProjectItem;
import com.connectcrew.teamone.api.project.ProjectMember;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FindProjectOutput {
    Mono<String> findProjectThumbnail(Long id);

    Flux<ProjectItem> findAllProjectItems(ProjectFilterOption option);

    Mono<ProjectDetail> find(Long projectId, Long userId);

    Mono<List<ProjectMember>> findMembers(Long projectId);
}
