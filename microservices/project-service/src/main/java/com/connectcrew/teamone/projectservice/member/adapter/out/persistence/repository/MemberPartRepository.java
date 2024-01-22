package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.MemberPartEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MemberPartRepository extends ReactiveCrudRepository<MemberPartEntity, Long> {

    Flux<MemberPartEntity> findAllByMember(Long memberId);

    Flux<Long> deleteAllByMemberAndIdNotIn(Long member, List<Long> ids);

    Flux<MemberPartEntity> deleteAllByMember(Long member);
}
