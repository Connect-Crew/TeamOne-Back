package com.connectcrew.teamone.projectservice.project.application.port.out;

import reactor.core.publisher.Mono;

public interface UpdateProjectOutput {
    Mono<Integer> updateFavorite(Long project, Integer change);
}
