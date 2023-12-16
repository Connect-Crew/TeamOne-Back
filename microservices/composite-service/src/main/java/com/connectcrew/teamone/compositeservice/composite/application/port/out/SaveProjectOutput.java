package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.api.project.ApplyInput;
import com.connectcrew.teamone.api.project.ProjectInput;
import com.connectcrew.teamone.api.project.ReportInput;
import reactor.core.publisher.Mono;

public interface SaveProjectOutput {
    Mono<Long> save(ProjectInput input);

    Mono<Boolean> save(ApplyInput input);

    Mono<Boolean> save(ReportInput input);
}
