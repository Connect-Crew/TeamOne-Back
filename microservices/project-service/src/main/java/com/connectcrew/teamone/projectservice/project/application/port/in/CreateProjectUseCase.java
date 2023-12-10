package com.connectcrew.teamone.projectservice.project.application.port.in;

import com.connectcrew.teamone.projectservice.project.application.port.in.command.CreateProjectCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.ReportCommand;
import reactor.core.publisher.Mono;

public interface CreateProjectUseCase {
    Mono<Long> create(CreateProjectCommand command);

    Mono<Boolean> report(ReportCommand command);
}
