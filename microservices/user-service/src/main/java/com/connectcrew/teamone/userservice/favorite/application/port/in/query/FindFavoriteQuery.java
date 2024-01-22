package com.connectcrew.teamone.userservice.favorite.application.port.in.query;

import com.connectcrew.teamone.api.userservice.favorite.FavoriteType;

public record FindFavoriteQuery(
        Long userId,
        FavoriteType favoriteType,
        Long target
) {
}
