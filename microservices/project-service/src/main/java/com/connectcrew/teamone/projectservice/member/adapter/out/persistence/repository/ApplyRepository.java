package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.ApplyEntity;
import com.connectcrew.teamone.projectservice.member.domain.enums.ApplyState;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplyRepository extends ReactiveCrudRepository<ApplyEntity, Long> {
    Mono<Boolean> existsByPartIdAndUser(Long partId, Long user);

    Flux<ApplyEntity> findAllByProjectAndState(Long project, ApplyState state);

    Flux<ApplyEntity> findAllByProjectAndUserAndState(Long project, Long user, ApplyState state);

    Flux<ApplyEntity> findAllByProjectAndPartIdAndState(Long project, Long partId, ApplyState state);
}
