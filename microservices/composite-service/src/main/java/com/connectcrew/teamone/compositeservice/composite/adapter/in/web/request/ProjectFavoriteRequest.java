package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

public record ProjectFavoriteRequest(
        Long project,
        Boolean favorite
) {
}
