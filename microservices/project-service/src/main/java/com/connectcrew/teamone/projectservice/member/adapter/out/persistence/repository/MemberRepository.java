package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.MemberEntity;
import com.connectcrew.teamone.projectservice.member.domain.enums.MemberState;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberRepository extends ReactiveCrudRepository<MemberEntity, Long> {

    Flux<MemberEntity> findAllByProjectId(Long project);

    @Query("SELECT COUNT(*) AS `count` FROM member AS m JOIN member_part AS mp ON m.id = mp.member JOIN part AS p ON mp.part = p.id WHERE p.id = :partId AND m.user = :user")
    Mono<Integer> countByPartIdAndUser(Long partId, Long user);

    Flux<MemberEntity> findAllByUser(Long userId);

    Mono<MemberEntity> findByProjectIdAndUser(Long project, Long user);

    Mono<Integer> countByProjectIdAndState(Long projectId, MemberState state);
}
