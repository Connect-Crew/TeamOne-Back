package com.connectcrew.teamone.userservice.favorite.application.port.out;

import com.connectcrew.teamone.userservice.favorite.domain.Favorite;
import com.connectcrew.teamone.userservice.favorite.domain.enums.FavoriteType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FindFavoriteOutput {
    Flux<Favorite> findAllByUserIdAndTypeAndTargets(Long userId, FavoriteType type, Long[] targets);

    Mono<Favorite> findByUserIdAndTypeAndTarget(Long userId, FavoriteType type, Long target);
    Mono<Boolean> existsByUserIdAndTypeAndTarget(Long userId, FavoriteType type, Long target);

}
