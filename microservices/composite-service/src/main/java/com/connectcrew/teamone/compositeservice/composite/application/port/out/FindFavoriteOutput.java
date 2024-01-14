package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.api.userservice.favorite.FavoriteType;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface FindFavoriteOutput {
    Mono<Boolean> isFavorite(Long userId, FavoriteType type, Long target);

    Mono<Map<Long, Boolean>> isFavorite(Long userId, FavoriteType type, List<Long> ids);
}
