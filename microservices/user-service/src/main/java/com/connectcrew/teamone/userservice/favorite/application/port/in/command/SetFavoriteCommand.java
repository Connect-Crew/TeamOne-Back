package com.connectcrew.teamone.userservice.favorite.application.port.in.command;

import com.connectcrew.teamone.userservice.favorite.domain.Favorite;
import com.connectcrew.teamone.api.userservice.favorite.FavoriteType;

public record SetFavoriteCommand(
        Long userId,
        FavoriteType favoriteType,
        Long target
) {
    public Favorite toDomain() {
        return Favorite.builder()
                .userId(userId)
                .favoriteType(favoriteType)
                .target(target)
                .build();
    }
}
