package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.ModifyProjectCommand;
import reactor.core.publisher.Mono;

public interface UpdateProjectUseCase {
    Mono<Long> update(ModifyProjectCommand command);
}
