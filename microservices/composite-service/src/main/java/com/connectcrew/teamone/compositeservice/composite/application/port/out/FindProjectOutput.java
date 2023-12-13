package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import reactor.core.publisher.Mono;

public interface FindProjectOutput {
    Mono<String> findProjectThumbnail(Long id);
}
