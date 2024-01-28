package com.connectcrew.teamone.compositeservice.wish.domain;

import com.connectcrew.teamone.api.wish.WishApiEvent;

import java.time.LocalDateTime;

public record Wish(
        Long userId,
        String message,
        LocalDateTime createdAt
) {

    public WishApiEvent toEvent() {
        return new WishApiEvent(
                userId,
                message,
                createdAt
        );
    }
}
