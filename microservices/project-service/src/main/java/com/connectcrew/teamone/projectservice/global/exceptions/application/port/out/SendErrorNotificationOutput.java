package com.connectcrew.teamone.projectservice.global.exceptions.application.port.out;


import com.connectcrew.teamone.projectservice.global.exceptions.domain.ErrorNotification;

public interface SendErrorNotificationOutput {
    void send(ErrorNotification notification);
}
