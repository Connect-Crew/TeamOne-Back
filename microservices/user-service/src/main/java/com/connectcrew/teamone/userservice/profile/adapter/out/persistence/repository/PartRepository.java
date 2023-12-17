package com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository;

import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity.PartEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PartRepository extends ReactiveCrudRepository<PartEntity, Long> {
    Flux<PartEntity> findAllByProfileId(Long profileId);
}
