package com.connectcrew.teamone.userservice.notification.application.port.out;

import com.connectcrew.teamone.userservice.notification.domain.FcmMessage;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface SendMessageOutput {
    boolean sendMessage(FcmMessage message) throws FirebaseMessagingException;
}
