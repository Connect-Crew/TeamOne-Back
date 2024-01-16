package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.ModifyProjectCommand;
import com.connectcrew.teamone.compositeservice.composite.domain.Apply;
import reactor.core.publisher.Mono;

public interface UpdateProjectUseCase {
    Mono<Long> update(ModifyProjectCommand command);

    Mono<Apply> acceptApply(Long applyId, Long userId, String leaderMessage);

    Mono<Apply> rejectApply(Long applyId, Long userId, String leaderMessage);
}
