package com.connectcrew.teamone.compositeservice.wish.application.port.in.command;

import com.connectcrew.teamone.compositeservice.wish.domain.Wish;

import java.time.LocalDateTime;

public record SendWishCommand(
        Long userId,
        String message
) {

    public Wish toDomain() {
        return new Wish(
                userId,
                message,
                LocalDateTime.now()
        );
    }
}
