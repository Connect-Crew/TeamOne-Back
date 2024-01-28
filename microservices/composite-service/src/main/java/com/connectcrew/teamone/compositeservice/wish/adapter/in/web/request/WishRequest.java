package com.connectcrew.teamone.compositeservice.wish.adapter.in.web.request;

import com.connectcrew.teamone.compositeservice.wish.application.port.in.command.SendWishCommand;

public record WishRequest(
        String message
) {

    public SendWishCommand toCommand(Long userId) {
        return new SendWishCommand(
                userId,
                message
        );
    }
}
