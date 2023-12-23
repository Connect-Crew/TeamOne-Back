package com.connectcrew.teamone.compositeservice.global.error.application.port.out;

import com.connectcrew.teamone.compositeservice.global.error.domain.ErrorNotification;

public interface SendErrorNotificationOutput {
    void send(ErrorNotification notification);
}
