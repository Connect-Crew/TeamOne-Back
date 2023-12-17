package com.connectcrew.teamone.compositeservice.composite.application.port.in;

import com.connectcrew.teamone.compositeservice.global.enums.FavoriteType;
import reactor.core.publisher.Mono;

public interface SaveUserUseCase {
    Mono<Boolean> setFavorite(Long userId, FavoriteType type, Long target);
}
