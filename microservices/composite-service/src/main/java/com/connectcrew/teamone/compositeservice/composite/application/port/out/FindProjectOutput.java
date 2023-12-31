package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.compositeservice.composite.domain.ProjectDetail;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectFilterOption;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectItem;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectMember;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FindProjectOutput {
    Mono<String> findProjectThumbnail(Long id);

    Flux<ProjectItem> findAllProjectItems(ProjectFilterOption option);

    Flux<ProjectItem> findAllProjectItems(Long userId);

    Mono<ProjectDetail> find(Long projectId, Long userId);

    Mono<List<ProjectMember>> findMembers(Long projectId);
}
