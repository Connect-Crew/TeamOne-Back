package com.connectcrew.teamone.userservice.repository;

import com.connectcrew.teamone.userservice.entity.FavoriteEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavoriteRepository extends ReactiveCrudRepository<FavoriteEntity, Long> {
    Flux<FavoriteEntity> findAllByProfileIdAndTypeAndTargetIn(Long profileId, String type, Flux<Long> target);

    Mono<Boolean> existsByProfileIdAndTypeAndTarget(Long profileId, String type, Long target);

    Mono<Boolean> deleteByProfileIdAndTypeAndTarget(Long profileId, String type, Long target);
}
