package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.compositeservice.composite.domain.Apply;
import com.connectcrew.teamone.compositeservice.composite.domain.Report;
import com.connectcrew.teamone.compositeservice.composite.domain.vo.CreateProjectInfo;
import reactor.core.publisher.Mono;

public interface SaveProjectOutput {
    Mono<Long> save(CreateProjectInfo input);

    Mono<Boolean> save(Apply input);

    Mono<Boolean> save(Report input);
}
