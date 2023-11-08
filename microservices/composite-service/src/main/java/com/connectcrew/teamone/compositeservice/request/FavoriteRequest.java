package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.api.user.favorite.FavoriteType;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface FavoriteRequest {
    Mono<Boolean> isFavorite(Long userId, FavoriteType type, Long target);

    Mono<Map<Long, Boolean>> isFavorite(Long userId, FavoriteType type, List<Long> ids);

    Mono<Boolean> setFavorite(Long userId, FavoriteType type, Long target);
}
