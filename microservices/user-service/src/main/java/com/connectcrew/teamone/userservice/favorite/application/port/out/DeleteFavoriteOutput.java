package com.connectcrew.teamone.userservice.favorite.application.port.out;

import com.connectcrew.teamone.userservice.favorite.domain.Favorite;
import reactor.core.publisher.Mono;

public interface DeleteFavoriteOutput {
    Mono<Favorite> delete(Favorite favorite);
}
