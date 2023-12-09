package com.connectcrew.teamone.userservice.notification.application.port.in.command;

import com.connectcrew.teamone.userservice.notification.domain.FcmMessage;
import com.connectcrew.teamone.userservice.notification.domain.MessageBody;

public record SendMessageCommand(
        Long userId,
        String title,
        MessageBody body,
        String deepLink
) {

    public FcmMessage toMessage(String fcm) {
        return FcmMessage.builder()
                .fcm(fcm)
                .title(title)
                .body(body)
                .deepLink(deepLink)
                .build();
    }
}
