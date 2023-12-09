package com.connectcrew.teamone.userservice.notification.adapter.out.messaging;

import com.connectcrew.teamone.userservice.notification.application.port.out.SendMessageOutput;
import com.connectcrew.teamone.userservice.notification.domain.FcmMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FcmMessageAdapter implements SendMessageOutput {
    private final FirebaseMessaging firebaseMessaging;
    private final ObjectMapper objectMapper;

    @Override
    public boolean sendMessage(FcmMessage message) throws FirebaseMessagingException {
        log.trace("send message: {}", message);
        try {
            Notification notification = Notification.builder()
                    .setTitle(message.title())
                    .setBody(objectMapper.writeValueAsString(message.body()))
                    .build();

            Message msg = Message.builder()
                    .setToken(message.fcm())
                    .setNotification(notification)
                    .build();

            firebaseMessaging.send(msg);
            return true;
        } catch (IOException e) {
            log.trace("send message - error: {}", e.getMessage(), e);
            return false;
        }
    }
}
