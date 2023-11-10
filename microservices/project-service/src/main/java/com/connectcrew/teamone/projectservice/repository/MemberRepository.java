package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.Member;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {

    @Query("SELECT m.* FROM member AS m JOIN part AS p ON m.part_id = p.id WHERE p.project = :project")
    Flux<Member> findAllByProject(@Param("project") Long project);
}
