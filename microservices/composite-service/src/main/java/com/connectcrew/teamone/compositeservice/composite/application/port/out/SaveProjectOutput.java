package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.api.projectservice.member.ApplyApiRequest;
import com.connectcrew.teamone.api.projectservice.project.CreateProjectApiRequest;
import com.connectcrew.teamone.api.projectservice.project.ReportApiRequest;
import reactor.core.publisher.Mono;

public interface SaveProjectOutput {
    Mono<Long> save(CreateProjectApiRequest input);

    Mono<Boolean> save(ApplyApiRequest input);

    Mono<Boolean> save(ReportApiRequest input);
}
