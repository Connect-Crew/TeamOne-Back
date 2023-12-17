package com.connectcrew.teamone.compositeservice.composite.application.port.in.command;


import com.connectcrew.teamone.compositeservice.composite.domain.FcmNotification;

public record SendNotificationCommand(
        Long userId,
        String title,
        String body
) {

    public FcmNotification toDomain() {
        return FcmNotification.builder()
                .userId(userId())
                .title(title())
                .body(body())
                .build();
    }
}
