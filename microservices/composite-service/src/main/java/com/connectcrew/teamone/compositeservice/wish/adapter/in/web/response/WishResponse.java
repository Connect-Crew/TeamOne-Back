package com.connectcrew.teamone.compositeservice.wish.adapter.in.web.response;

import com.connectcrew.teamone.compositeservice.wish.domain.Wish;

public record WishResponse(
        String message
) {

    public static WishResponse of(Wish wish) {
        return new WishResponse(
                wish.message()
        );
    }
}
