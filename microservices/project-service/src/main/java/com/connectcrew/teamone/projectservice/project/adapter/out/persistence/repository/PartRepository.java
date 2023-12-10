package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.PartEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PartRepository extends ReactiveCrudRepository<PartEntity, Long> {
    Flux<PartEntity> findAllByProject(Long project);

    Mono<PartEntity> findByProjectAndPart(Long project, String part);

    @Query("SELECT p.* FROM part AS p JOIN member AS m ON p.id = m.part_id WHERE p.project = :project AND m.user = :user")
    Flux<PartEntity> findAllByProjectAndUser(Long project, Long user);
}
