package com.connectcrew.teamone.userservice.favorite.application.port.in.query;

import com.connectcrew.teamone.userservice.favorite.domain.enums.FavoriteType;

public record FindFavoritesQuery(
        Long userId,
        FavoriteType favoriteType,
        Long[] targets
) {
}
