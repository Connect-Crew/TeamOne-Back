package com.connectcrew.teamone.userservice.repository;

import com.connectcrew.teamone.userservice.entity.RepresentProjectEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RepresentProjectRepository extends ReactiveCrudRepository<RepresentProjectEntity, Long> {

    Flux<RepresentProjectEntity> findAllByProfileId(Long profileId);
}
