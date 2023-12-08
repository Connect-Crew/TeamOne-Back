package com.connectcrew.teamone.userservice.favorite.adapter;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface FavoriteRepository extends ReactiveCrudRepository<FavoriteEntity, Long> {
    Flux<FavoriteEntity> findAllByProfileIdAndTypeAndTargetIn(Long profileId, String type, Collection<Long> target);

    Mono<Boolean> existsByProfileIdAndTypeAndTarget(Long profileId, String type, Long target);

    Mono<Boolean> deleteByProfileIdAndTypeAndTarget(Long profileId, String type, Long target);
}
