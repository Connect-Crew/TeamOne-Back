package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.ApplyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplyRepository extends ReactiveCrudRepository<ApplyEntity, Long> {
    Mono<Boolean> existsByPartIdAndUser(Long partId, Long user);

    Flux<ApplyEntity> findAllByProject(Long project);

    Flux<ApplyEntity> findAllByProjectAndUser(Long project, Long user);

    Flux<ApplyEntity> findAllByProjectAndPartId(Long project, Long partId);
}
