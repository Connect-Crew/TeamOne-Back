package com.connectcrew.teamone.userservice.favorite.application.port.out;

import com.connectcrew.teamone.userservice.favorite.domain.Favorite;
import reactor.core.publisher.Mono;

public interface SaveFavoriteOutput {
    Mono<Favorite> save(Favorite favorite);
}
