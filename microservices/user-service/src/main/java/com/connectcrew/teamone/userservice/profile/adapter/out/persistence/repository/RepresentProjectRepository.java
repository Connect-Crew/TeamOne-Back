package com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository;

import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity.RepresentProjectEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RepresentProjectRepository extends ReactiveCrudRepository<RepresentProjectEntity, Long> {

    Flux<RepresentProjectEntity> findAllByProfileId(Long profileId);
}
