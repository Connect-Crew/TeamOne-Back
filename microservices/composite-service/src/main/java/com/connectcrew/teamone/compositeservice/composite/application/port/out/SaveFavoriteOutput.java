package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.compositeservice.global.enums.FavoriteType;
import reactor.core.publisher.Mono;

public interface SaveFavoriteOutput {
    Mono<Boolean> setFavorite(Long userId, FavoriteType type, Long target);
}
