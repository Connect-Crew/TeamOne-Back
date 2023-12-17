package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

public record FavoriteResponse(
        Long project,
        Boolean myFavorite,
        Integer favorite
) {
}
