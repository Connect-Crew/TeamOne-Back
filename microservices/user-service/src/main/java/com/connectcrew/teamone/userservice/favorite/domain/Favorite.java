package com.connectcrew.teamone.userservice.favorite.domain;

import com.connectcrew.teamone.userservice.favorite.domain.enums.FavoriteType;
import lombok.Builder;

@Builder
public record Favorite(
        Long favoriteId,
        Long userId,
        FavoriteType favoriteType,
        Long target
) {


}
