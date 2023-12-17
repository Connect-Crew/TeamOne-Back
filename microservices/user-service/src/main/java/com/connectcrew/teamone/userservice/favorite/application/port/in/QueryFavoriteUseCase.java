package com.connectcrew.teamone.userservice.favorite.application.port.in;

import com.connectcrew.teamone.userservice.favorite.application.port.in.query.FindFavoriteQuery;
import com.connectcrew.teamone.userservice.favorite.application.port.in.query.FindFavoritesQuery;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface QueryFavoriteUseCase {
    Mono<Map<Long, Boolean>> findMapByCommand(FindFavoritesQuery query);

    Mono<Boolean> findByCommand(FindFavoriteQuery query);
}
