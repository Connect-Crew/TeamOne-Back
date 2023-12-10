package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.MemberEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberRepository extends ReactiveCrudRepository<MemberEntity, Long> {

    @Query("SELECT m.* FROM member AS m JOIN part AS p ON m.part_id = p.id WHERE p.project = :project")
    Flux<MemberEntity> findAllByProject(@Param("project") Long project);

    Mono<Boolean> existsByPartIdAndUser(Long partId, Long user);
}
