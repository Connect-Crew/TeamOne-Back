package com.connectcrew.teamone.userservice.favorite.domain;

import com.connectcrew.teamone.api.favorite.FavoriteType;
import lombok.Builder;

@Builder
public record Favorite(
        Long favoriteId,
        Long userId,
        FavoriteType favoriteType,
        Long target
) {


}
