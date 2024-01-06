package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.MemberEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberRepository extends ReactiveCrudRepository<MemberEntity, Long> {

    Flux<MemberEntity> findAllByProjectId(Long project);

    @Query("SELECT m.* FROM member AS m JOIN part AS p ON m.part_id = p.id WHERE p.id = :partId AND m.user = :user")
    Mono<Boolean> existsByPartIdAndUser(Long partId, Long user);

    Flux<MemberEntity> findAllByUser(Long userId);

    Mono<MemberEntity> findByProjectIdAndUser(Long project, Long user);
}
