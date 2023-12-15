package com.connectcrew.teamone.compositeservice.composite.application.port.out;

import com.connectcrew.teamone.api.user.notification.FcmNotification;

public interface SendNotificationOutput {

    void send(FcmNotification notification);
}
