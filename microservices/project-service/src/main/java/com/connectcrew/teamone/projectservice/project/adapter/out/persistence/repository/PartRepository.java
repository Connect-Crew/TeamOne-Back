package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.PartEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface PartRepository extends ReactiveCrudRepository<PartEntity, Long> {

    Flux<PartEntity> findAllById(@NotNull Iterable<Long> ids);
    Flux<PartEntity> findAllByProject(Long project);

    Mono<PartEntity> findByProjectAndPart(Long project, String part);

    Flux<PartEntity> deleteAllByProjectAndIdNotIn(Long project, Collection<Long> ids);

    @Query("SELECT p.* FROM part AS p JOIN member AS m ON p.id = m.part_id WHERE p.project = :project AND m.user = :user")
    Flux<PartEntity> findAllUserPartByProjectAndUser(Long project, Long user);

    @Query("SELECT p.* FROM part AS p JOIN apply AS a ON p.id = a.part_id WHERE p.project = :project AND a.user = :user")
    Flux<PartEntity> findAllAppliedPartByProjectAndUser(Long project, Long user);

    @Query("SELECT pt.* FROM part AS pt JOIN project AS p ON pt.project = p.id JOIN member_part AS mp ON pt.id = mp.part JOIN member AS m ON mp.member = m.id WHERE p.id = :project AND m.user = p.leader")
    Flux<PartEntity> findAllLeaderPartByProject(Long project);
}
