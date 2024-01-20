package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.api.projectservice.enums.ProjectState;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.KickCommand;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.ModifyProjectCommand;
import com.connectcrew.teamone.compositeservice.composite.domain.Apply;
import com.connectcrew.teamone.compositeservice.composite.domain.ProjectMember;
import reactor.core.publisher.Mono;

public interface UpdateProjectUseCase {
    Mono<Long> update(ModifyProjectCommand command);

    Mono<ProjectState> updateProjectState(Long userId, Long projectId, ProjectState projectState);

    Mono<ProjectState> deleteProjectState(Long userId, Long projectId);

    Mono<Apply> acceptApply(Long applyId, Long userId, String leaderMessage);

    Mono<Apply> rejectApply(Long applyId, Long userId, String leaderMessage);

    Mono<ProjectMember> kickMember(KickCommand kickCommand);
}
