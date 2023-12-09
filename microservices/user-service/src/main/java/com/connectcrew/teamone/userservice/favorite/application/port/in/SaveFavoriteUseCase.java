package com.connectcrew.teamone.userservice.favorite.application.port.in;

import com.connectcrew.teamone.userservice.favorite.application.port.in.command.SetFavoriteCommand;
import reactor.core.publisher.Mono;

public interface SaveFavoriteUseCase {
    Mono<Boolean> setFavorite(SetFavoriteCommand command);
}
