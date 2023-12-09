package com.connectcrew.teamone.userservice.favorite.application.port.in;

import com.connectcrew.teamone.userservice.favorite.application.port.in.command.SaveFavoriteCommand;
import reactor.core.publisher.Mono;

public interface SaveFavoriteUseCase {
    Mono<Boolean> saveFavorite(SaveFavoriteCommand command);
}
