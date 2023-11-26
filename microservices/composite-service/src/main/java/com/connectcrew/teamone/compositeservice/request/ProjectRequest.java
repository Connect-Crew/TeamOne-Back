package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.api.project.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProjectRequest {
    Flux<ProjectItem> getProjectList(ProjectFilterOption option);

    Mono<ProjectDetail> getProjectDetail(Long projectId, Long userId);

    Mono<List<ProjectMember>> getProjectMembers(Long projectId);

    Mono<Long> saveProject(ProjectInput input);

    Mono<Boolean> applyProject(ApplyInput input);

    Mono<Boolean> reportProject(ReportInput input);

    Mono<Integer> updateFavorite(FavoriteUpdateInput input);

    Mono<String> getProjectThumbnail(Long projectId);
}
