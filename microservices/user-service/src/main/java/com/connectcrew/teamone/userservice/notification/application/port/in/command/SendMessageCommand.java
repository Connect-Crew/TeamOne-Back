package com.connectcrew.teamone.userservice.notification.application.port.in.command;

import com.connectcrew.teamone.api.userservice.notification.push.SendMessageEvent;
import com.connectcrew.teamone.userservice.notification.domain.FcmMessage;
import com.connectcrew.teamone.userservice.notification.domain.MessageBody;

public record SendMessageCommand(
        Long userId,
        String title,
        String body,
        String deepLink
) {

    public static SendMessageCommand from(SendMessageEvent event) {
        return new SendMessageCommand(event.userId(), event.title(), event.body(), event.deepLink());
    }

    public FcmMessage toMessage(String fcm) {
        return FcmMessage.builder()
                .fcm(fcm)
                .title(title)
                .body(new MessageBody(body, deepLink))
                .build();
    }
}
