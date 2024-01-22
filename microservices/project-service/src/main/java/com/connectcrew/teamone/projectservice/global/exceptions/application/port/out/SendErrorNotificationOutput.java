package com.connectcrew.teamone.projectservice.global.exceptions.application.port.out;


import com.connectcrew.teamone.api.userservice.notification.error.ErrorNotification;

public interface SendErrorNotificationOutput {
    void send(ErrorNotification notification);
}
