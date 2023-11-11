package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.Part;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PartRepository extends ReactiveCrudRepository<Part, Long> {
    Flux<Part> findAllByProject(Long project);

    Mono<Part> findByProjectAndPart(Long project, String part);
}
