package com.connectcrew.teamone.projectservice.project.application.port.out;

import reactor.core.publisher.Mono;

public interface UpdateProjectOutput {
    Mono<Integer> favorite(Long project, Integer change);

    Mono<Long> updateCollected(Long partId, Integer change);
}
