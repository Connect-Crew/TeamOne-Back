package com.connectcrew.teamone.api.project;

public record FavoriteUpdateInput(
        Long project,
        Integer favorite
) {
}
