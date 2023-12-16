package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.api.user.favorite.FavoriteType;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface QueryUserUseCase {
    Mono<Boolean> isFavorite(Long userId, FavoriteType type, Long target);

    Mono<Map<Long, Boolean>> isFavorite(Long userId, FavoriteType type, List<Long> ids);
}
