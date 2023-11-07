package com.connectcrew.teamone.userservice.repository;

import com.connectcrew.teamone.userservice.entity.PartEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PartRepository extends ReactiveCrudRepository<PartEntity, Long> {
    Flux<PartEntity> findAllByProfileId(Long profileId);
}
