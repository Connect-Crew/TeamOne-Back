package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.query.FindProjectListQuery;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectDetail;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectItem;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectMember;
import reactor.core.publisher.Mono;

import java.util.List;

public interface QueryProjectUseCase {

    Mono<List<ProjectItem>> getProjectList(FindProjectListQuery query);

    Mono<ProjectDetail> find(Long id, Long userId);

    Mono<List<ProjectMember>> getProjectMemberList(Long projectId);
}
