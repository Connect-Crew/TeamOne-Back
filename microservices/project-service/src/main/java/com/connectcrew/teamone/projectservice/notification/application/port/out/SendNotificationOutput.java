package com.connectcrew.teamone.projectservice.notification.application.port.out;

import com.connectcrew.teamone.projectservice.notification.domain.Notification;

public interface SendNotificationOutput {
    void send(Notification notification);
}
